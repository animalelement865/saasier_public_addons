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

import time
from openerp.report import report_sxw
from openerp.osv import osv
from openerp import pooler
from datetime import datetime
import urllib
import base64
class label_generate_new(report_sxw.rml_parse):
    
    
    def __init__(self, cr, uid, name, context):
        super(label_generate_new, self).__init__(cr, uid, name, context=context)
        self.localcontext.update({
            'time': time,
            'user': self.pool.get('res.users').browse(cr, uid, uid, context),
            'get_label':self._get_label,
            'get_product_name' : self.get_product_name,
            'get_serial_number' : self.get_serial_number,
            'get_line_data':self.get_line_data,
            'get_pack_list':self.get_pack_list,
            #'get_image':self.get_image,
            # 'get_amount_total':self.get_amount_total,
        })
        #Marque the Picking.label as printed
        self.pool.get('stock.picking.out.label').write(cr, uid, context['active_ids'],{'is_label_printed': True},context=context)

    def get_line_data(self, picking_id, tracking_id):
        self.cr.execute("""select product_id, product_qty, prodlot_id from stock_move where picking_id=%s and tracking_id=%s""",(picking_id,tracking_id,))
        res = self.cr.fetchall()
        return res or ''
        
    def get_pack_list(self, picking_id):

        self.cr.execute("""select DISTINCT ON (tracking_id) id, tracking_id, picking_id from stock_move where picking_id=%s""",(picking_id,))
        res = self.cr.fetchall()
        return res or ''
    
    def get_product_name(self,product_id):
         
        self.cr.execute("""select name, list_price from product_template where id=%s""",(product_id,))
        res = self.cr.fetchall()
        return res[0][0] or ''
    
    def get_serial_number(self,prodlot_id):
         
        self.cr.execute("""select name from stock_production_lot where id=%s""",(prodlot_id,))
        res = self.cr.fetchall()
        if res:
            return res[0][0]
        else:
            return '----'

    # def get_amount_total(self,move_id):
         
        # self.cr.execute("""select sum(price_unit * product_qty) from stock_move where id=%s""",(move_id,))
        # res = self.cr.fetchall()
        # return res[0][0] or ''
    

#     def get_image(self, picking_id):
#         
#         self.cr.execute()
#         
#         self.pool.get('ir.attachment').browse(cr, uid, )
#     
#    #     j = """<image file="/opt/openerp/server/filename.png" width="300" height="500" ></image>"""
#         return True   
#     

    def _get_label(self, move_id):
         
        ###### used for warehouse domain added by 4devnet #####
        self.cr.execute("""select location_id from stock_move where id=%s""",(move_id,))
        loc_id = self.cr.fetchall()
        self._new_warehouse_id = loc_id[0][0]
 
        self.cr.execute("select tracking_id,db_datas from ir_attachment where res_model='stock.move' and res_id=%s order by id",(move_id,))
        res = self.cr.fetchall()
        return res
        
report_sxw.report_sxw('report.label.generate','stock.picking.out.label','addons/shipping_saasier/report/label_generate.rml',parser=label_generate_new)

# vim:expandtab:smartindent:tabstop=4:softtabstop=4:shiftwidth=4:

