# -*- coding: utf-8 -*-
##############################################################################
#
#    OpenERP, Open Source Management Solution
#    Copyright (C) 2004-2010 Tiny SPRL (<http://tiny.be>).
#
#    This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU Affero General Public License as
#    published by the Free Software Foundation, either version 3 of the
#    License, or (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU Affero General Public License for more details.
#
#    You should have received a copy of the GNU Affero General Public License
#    along with this program.  If not, see <http://www.gnu.org/licenses/>.
#
##############################################################################
from openerp.osv import fields, osv
import os
import time
from openerp.tools.misc import DEFAULT_SERVER_DATETIME_FORMAT
import base64
class dispatch_consignment(osv.osv_memory):
    _name = 'dispatch.consignment'
    _columns = {
            'batch': fields.char('Batch', size=10)
    }
    
    def default_get(self, cr, uid, fields, context=None):
        stock_obj=self.pool.get('stock.picking.out')
        res = super(dispatch_consignment, self).default_get(cr, uid, fields, context=context)
        res.update({'batch': self.pool.get('ir.sequence').get(cr, uid, 'dispatch.consignment')})
        stock_obj.write(cr,uid,context['active_ids'],{'batch_no':self.pool.get('ir.sequence').get(cr, uid, 'dispatch.consignment')})
        return res

    def stock_order(self, cr, uid,ids, context={}):
        res = {}
        stock_obj=self.pool.get('stock.picking.out')
        print context
        print context['active_ids']
        res = stock_obj.create_shipment_simplest(cr,uid,context['active_ids'],context)
        return res
    
    def manifest(self, cr, uid, ids, context={}):
        print "======="
        return self.pool.get('stock.picking.out').delivered_consignment(cr, uid, [], context={})
    
    def dispatch_consig(self, cr, uid, ids, context={}):
        picking_object_pool = self.pool.get('stock.picking.out')
        picking_object = self.pool.get('stock.picking')
        consig_obj_pool = self.pool.get('consignment.consignment')
        sale_obj_pool = self.pool.get('sale.order')
        carrier_obj = self.pool.get('delivery.carrier')
        product_object = self.pool.get('product.product')
        partner_object = self.pool.get('res.partner')
        attach_object = self.pool.get('ir.attachment')
        par_obj = self.pool.get('parcel.tracking')
        para = self.pool.get("ir.config_parameter")
        stock_partial_obj = self.pool.get('stock.partial.picking')
        merge_obj = self.pool.get('merge.pdf')
        re = True
        if context.get('active_ids'):
            url = para.get_param(cr, uid, "metapack.url", context=context)
            user = para.get_param(cr, uid, "metapack.user", context=context)
            pwd = para.get_param(cr, uid, "metapack.password", context=context)
            ip = para.get_param(cr, uid, "metapack.ip_address", context=context)
            disp_obj = self.browse(cr, uid, ids[0], context=context)
            meta_pack = API(url, user, pwd)
            meta_pack.connect()
            stock_partial_ids = []
            for picking in picking_object_pool.browse(cr, uid, context.get('active_ids')):
                if not picking.batch_code:
                    s_ids = sale_obj_pool.search(cr, uid, [('name', '=', picking.origin)])
                    sale = sale_obj_pool.browse(cr, uid, s_ids[0])
                    consignment = consig_obj_pool.create_consign(cr, uid,picking, sale, disp_obj.batch)
                    properties = consig_obj_pool.create_properties()
                    if consignment:
                        try:
                            res = meta_pack.client.service.despatchConsignment(consignment,{},False, properties)
                        except Exception, e:
                            print "=========ggggggggg===>",
                            picking_object_pool.write(cr, uid, picking.id, {'faulty':True, 'error_note':str(e)})
                            continue
                        print "=======res======>",res
                        carrier_name = res['consignment']['carrierName']
                        carr_ids = carrier_obj.search(cr, uid, [('name','=',carrier_name)])
                        carrier_code = res['consignment']['carrierCode']
                        if carr_ids:
                            c_id = carr_ids[0]
                            #carrier_obj.write(cr, uid, c_id,{'metapack_carrier_code': carrier_code})
                        else:
                            prod_ids = product_object.search(cr, uid, [('name', '=','Shipping and Handling')])
                            if prod_ids:
                                p_id  = prod_ids[0]
                            else: 
                                p_id  = product_object.create(cr, uid, {'name': 'Shipping and Handling', 'type':"service" ,'categ_id': 1})
                            patner_id = partner_object.create(cr, uid, {'name': carrier_name})
                            c_id = carrier_obj.create(cr, uid, {'name': carrier_name , 'metapack_carrier_code':carrier_code, 'product_id': p_id,'partner_id': patner_id})
                        #tracking_code = res['consignment']['parcels'][0]['trackingCode']
                        par_len = len(res['consignment']['parcels'])
                        i = 0
                        while par_len > 0:
                            par_len = par_len - 1
                            track = {
                                'picking_id': picking.id,
                                'tracking_code': res['consignment']['parcels'][i]['trackingCode']
                            }
                            par_obj.create(cr, uid, track)
                            i = i + 1
                        picking_object_pool.write(cr, uid, picking.id, {'metapack_carrier_code': c_id, 'batch_code':disp_obj.batch, 'faulty': False, 'error_note':''})
                        pdf = res['paperwork']['labels']
                        #pdf = res['labels']
                        file = "/var/www/label"+str(picking.id)+".pdf"
                        file1 = "label" +str(picking.id)+".pdf"
                        if os.path.exists(file):
                            os.remove(file)
                        pdf_file= open(file,"wb")
                        pdf_file.write(base64.decodestring(pdf))
                        pdf_file.close()
                        a_id = attach_object.create(cr, uid, {'name': 'label', 'datas_fname': file, 'res_model': 'stock.picking.out', 'res_id': picking.id, 'type': 'url', 'url': "http://"+ ip+ "/"+file1})
#                        date=time.strftime(DEFAULT_SERVER_DATETIME_FORMAT)
#                        partial_data = {'delivery_date':date}
#                        for move in picking.move_lines:
#                            partial_data['move%s' % (move.id)] = {
#                                'product_id': move.product_id.id,
#                                'product_qty': move.product_qty,
#                                'product_uom': move.product_uom.id,
#                                'prodlot_id': move.prodlot_id.id,
#                            }
#                        picking_object.do_partial(cr, uid, [picking.id], partial_data, context=context)
                        picking.write({'state':'awaiting_dispatch'})
                        cr.commit()
                    else:
                        picking.write({'faulty': True})
            context.update({'batch':disp_obj.batch})
            re = merge_obj.print_lable(cr, uid, ids, context=context)
            print "=====re======>",re
            #re = re['url']
            #url1 = "http://"+ ip_address + "/"+batch + ".pdf"
        return re
dispatch_consignment()   