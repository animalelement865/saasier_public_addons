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

import base64
import netsvc
from openerp.osv import fields, osv
from openerp import tools
from qrcode import *
import os

class stock_move(osv.osv):
    _inherit = "stock.move"
    _columns = {
             
        'prodlot_id': fields.many2one('stock.production.lot', 'Serial Number', states={'done': [('readonly', True)]}, help="Serial number is used to put a serial number on the production", select=True),
        'qr_code': fields.char('QR Code', size=100, readonly=True),
        'qr_img': fields.binary('Data'),
        'mo_id' : fields.char('MO ID'),
        'err_correction': fields.selection([(ERROR_CORRECT_L, 'ERROR_CORRECT_L'),
                                   (ERROR_CORRECT_M,'ERROR_CORRECT_M'), # used by cash statements
                                   (ERROR_CORRECT_Q, 'ERROR_CORRECT_Q'),
                                   (ERROR_CORRECT_H, 'ERROR_CORRECT_H')],
                                   'Error Correction')
                }
    _defaults = {
                 'err_correction': ERROR_CORRECT_L,
                 }
    
    def create(self, cr, uid, vals, context=None):
             
        if vals.get('picking_id', False):
            pick_obj = self.pool.get('stock.picking.in').browse(cr,uid,vals['picking_id'])
            new_id = super(stock_move, self).create(cr, uid, vals, context)
            self.write(cr, uid, [new_id], {'qr_code': pick_obj.name + '_' + str(new_id)})
        else:
            new_id = super(stock_move, self).create(cr, uid, vals, context)
            self.write(cr, uid, [new_id], {'qr_code': '0000' + '_' + str(new_id)})
        
        return new_id
    
      
    def generate_image(self, cr, uid, ids, context=None):
        "button function for genrating image """
        if not context:
            context = {}
        for self_obj in self.browse(cr, uid, ids, context=context):

            if self_obj.prodlot_id.id:
                        
                    qr = QRCode(version=2, error_correction=self_obj.err_correction, box_size=3)
                    #qr.add_data(self_obj.prodlot_id.name)
                    #qr.make() # Generate the QRCode itself
            
            
                    cr.execute(""" select name from stock_picking where id=%s""",(self_obj.picking_id.id,))
                    picking_name = cr.fetchall()
                    ref_lot = picking_name[0][0] + '_' + self_obj.prodlot_id.name
                    cr.execute(""" update stock_production_lot set ref=%s where id=%s""",(ref_lot,self_obj.prodlot_id.id,))
            
          
                    qr.add_data(ref_lot)
                    qr.make()
            # im contains a PIL.Image.Image object
                    im = qr.make_image()
            
            # To save it
           # im.save("/opt/openerp/server/filename.png")
	    # for local
            	    a = str(os.path.dirname(os.path.abspath(__file__))) + "/filename.png"
            	    im.save(a)
            	    fdesc = open(a,"rb")
            # for local
	        #fdesc = open("filename.png","rb")
                    data = base64.encodestring(fdesc.read())
            
                    self.write(cr, uid, self_obj.id,
                               {'qr_img':data},context=context)
            
           # super(stock_move,self).write(cr,uid,self_obj.id,context=context)
            else:
                raise osv.except_osv('Message !!!','Please Generate Serial Number First....')
        
        return True
    
class stock_location(osv.osv):
    
    _inherit = "stock.location"
    
    _columns = {
      #  'qr_code': fields.char('QR Code', size=100),
        'location_qr_img': fields.binary('Data'),
        'err_correction': fields.selection([(ERROR_CORRECT_L, 'ERROR_CORRECT_L'),
                                   (ERROR_CORRECT_M,'ERROR_CORRECT_M'), # used by cash statements
                                   (ERROR_CORRECT_Q, 'ERROR_CORRECT_Q'),
                                   (ERROR_CORRECT_H, 'ERROR_CORRECT_H')],
                                   'Error Correction')
                }

    _defaults = {
                 'err_correction': ERROR_CORRECT_L,
                 }
    
    
    def generate_image_stock(self, cr, uid, ids, context=None):
        "button function for genrating image """
        if not context:
            context = {}

        for self_obj in self.browse(cr, uid, ids, context=context):

#             if self_obj.location_id.name:
#                 
#                 code = self_obj.location_id.name + '/' + self_obj.name
#             else:
#                 code = self_obj.name
            m = 'LOC/'
            code = str(m) + str(ids[0])
           
            qr = QRCode(version=2, error_correction=self_obj.err_correction, box_size=3)
            qr.add_data(code)
            qr.make() # Generate the QRCode itself
            
            # im contains a PIL.Image.Image object
            im = qr.make_image()
            
            # To save it
           # im.save("/opt/openerp/server/filename.png")
        # for local
            a = str(os.path.dirname(os.path.abspath(__file__))) + "/filename.png"
            im.save(a)
            fdesc = open(a,"rb")
            # for local
            #fdesc = open("filename.png","rb")
            data = base64.encodestring(fdesc.read())
            
            self.write(cr, uid, self_obj.id,
                {'location_qr_img':data},context=context)
        
        return True


class mrp_production(osv.osv):
    
    _inherit = 'mrp.production'
    
    
    def action_confirm(self, cr, uid, ids, context=None):
        """ Confirms production order.
        @return: Newly generated Shipment Id.
        """
        shipment_id = False
        wf_service = netsvc.LocalService("workflow")
        uncompute_ids = filter(lambda x:x, [not x.product_lines and x.id or False for x in self.browse(cr, uid, ids, context=context)])
        self.action_compute(cr, uid, uncompute_ids, context=context)
        for production in self.browse(cr, uid, ids, context=context):
            shipment_id = self._make_production_internal_shipment(cr, uid, production, context=context)
            produce_move_id = self._make_production_produce_line(cr, uid, production, context=context)

            # Take routing location as a Source Location.
            source_location_id = production.location_src_id.id
            if production.bom_id.routing_id and production.bom_id.routing_id.location_id:
                source_location_id = production.bom_id.routing_id.location_id.id

            for line in production.product_lines:
                consume_move_id = self._make_production_consume_line(cr, uid, line, produce_move_id, source_location_id=source_location_id, context=context)
                if shipment_id:
                    shipment_move_id = self._make_production_internal_shipment_line(cr, uid, line, shipment_id, consume_move_id,\
                                 destination_location_id=source_location_id, context=context)
                    self._make_production_line_procurement(cr, uid, line, shipment_move_id, context=context)

            if shipment_id:
                wf_service.trg_validate(uid, 'stock.picking', shipment_id, 'button_confirm', cr)
            production.write({'state':'confirmed'}, context=context)
        cr.execute(""" select move_id from mrp_production_move_ids where production_id=%s""",(ids[0],))
        result123 = cr.fetchall()
      
        for res12 in result123:
            cr.execute(""" update stock_move set mo_id=%s where id=%s""",(ids[0],res12[0],))
         
        return shipment_id
   
