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
from openerp.osv import fields, osv

class sale_details(osv.osv_memory):
    _name = 'sale.details'
    _description = 'Sale detail report wizard'

    _columns = {
        'name' : fields.char('Name'),
        'category_id':fields.many2one('product.category','Category',domain = [],required=True),
        'date_from': fields.date('From', required=True),
        'date_to': fields.date('To', required=True),
#         'start_date' : fields.date('Date From',required="True"),
#         'end_date' : fields.date('Date To',required="True"),
#         'type' : fields.selection([('categ', 'Product Category'), ('prod', 'Product')], 'Filter By'),
#         'parent_cate_id' : fields.many2one('product.category', 'Parent Category'),
#         'product_ids' : fields.many2many('product.product', 'prod_sales_rel', 'wiz_id', 'product_id', 'Products'),

    }
    
    
    
    def print_report(self, cr, uid, ids, data, context=None):
        sale_order_obj = self.pool.get('sale.order')
        datas = {}
        if context is None:
            context = {}
        data = self.read(cr, uid, ids)[0]
        datas={
               'ids':[],
               'model':'sale.order',
               'form':data
              }
        return {'type': 'ir.actions.report.xml', 'report_name': 'sale.details', 'datas': data}

    _defaults = {
        'date_from': lambda *a: time.strftime('%Y-%m-01'),
        'date_to': lambda *a: time.strftime('%Y-%m-%d'),
        'category_id': 1,
        #'type': lambda *a: 'categ',
    }
    

sale_details()
                


# vim:expandtab:smartindent:tabstop=4:softtabstop=4:shiftwidth=4:
