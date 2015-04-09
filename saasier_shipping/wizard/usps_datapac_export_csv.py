# -*- encoding: utf-8 -*-
##############################################################################
#
# Copyright (c) 2012 Omar Castiñeira Saavedra <omar@pexego.es>
#                         Pexego Sistemas Informáticos http://www.pexego.es
# Copyright (C) 2013 Tadeus Prastowo <tadeus.prastowo@infi-nity.com>
#                         Vikasa Infinity Anugrah <http://www.infi-nity.com>
#
# WARNING: This program as such is intended to be used by professional
# programmers who take the whole responsability of assessing all potential
# consequences resulting from its eventual inadequacies and bugs
# End users who are looking for a ready-to-use solution with commercial
# garantees and support are strongly adviced to contract a Free Software
# Service Company
#
# This program is Free Software; you can redistribute it and/or
# modify it under the terms of the GNU General Public License
# as published by the Free Software Foundation; either version 2
# of the License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
#
##############################################################################

import base64

try:
    import release
    import pooler
    from osv import osv,fields
    from tools.translate import _
except ImportError:
    import openerp
    from openerp import release
    from openerp import pooler
    from openerp.osv import osv,fields
    from openerp.tools.translate import _

class datapac_create_data_template(osv.osv_memory):
    _name = 'datapac.create.data.template'
    _description = 'Create DataPac CSV File'

    def action_create_csv(self, cr, uid, ids, context=None):
        this = self.browse(cr, uid, ids)[0]
        stock_label_obj = self.pool.get('stock.picking.out.label')
        stock_move_obj  = self.pool.get('stock.move')
        stock_label_record = stock_label_obj.browse(cr, uid, context['active_id'], context=context)
        csv = '"Name","ToAddress","City","State","Zip","ZipPlusFour","Quantity","Item","Description","Weight","Shipped","BackOrdered","UspsClass","UspsPackaging","Height","Width","Length","ReturnAddress","SoldToAddress","Cost","CustomerNumber","OrderNumber","PackageID"\n'
        for delivery_record in  stock_label_record.picking_ids:
            if delivery_record.shipping_carrier!="USPS DataPac CSV":
                continue

            for move in delivery_record.move_lines:
                csv_line = '"'

                # Name
                csv_line = csv_line + str(delivery_record.partner_id.name or '') + '","'
                # ToAddress
                csv_line = csv_line + str(delivery_record.partner_id.street + (delivery_record.partner_id.street2 or '')) + '","'
                # City
                csv_line = csv_line + str(delivery_record.partner_id.city or '') + '","'
                # State
                csv_line = csv_line + str(delivery_record.partner_id.state_id.code or '') + '","'
                # Zip
                csv_line = csv_line + str(delivery_record.partner_id.zip) + '","'
                # ZipPlusFour
                csv_line = csv_line + '","'
                # Quantity
                csv_line = csv_line + str(move.product_qty or '') + '","'
                # Item
                csv_line = csv_line + str(move.product_id.default_code or '') + '","'
                # Description
                csv_line = csv_line + str(move.name or '') + '","'
                # Weight
                csv_line = csv_line + str(move.override_weight or '') + '","'
                # Shipped
                csv_line = csv_line + str(move.product_qty or '') + '","'
                # BackOrdered
                csv_line = csv_line + '","'
                # UspsClass
                csv_line = csv_line + str(delivery_record.shipping_service_usps or '') + '","'
                # UspsPackaging
                csv_line = csv_line + str(delivery_record.shipping_packaging_usps or '') + '","'

                #TODO : SHOULD KNOW FROM WHERE TO GET DIMENSIONS, FROM PACK SETUP BOX, OR FROM PRODUCT INFORMATIONS.

                # Height
                csv_line = csv_line + str(move.product_id.height or '') + '","'               
                # Width
                csv_line = csv_line + str(move.product_id.width or '') + '","'      
                # Length
                csv_line = csv_line + str(move.product_id.length or '') + '","' 
                # ReturnAddress
                csv_line = csv_line + str(delivery_record.partner_id.street + ( delivery_record.partner_id.street2 or '' )) + '","' 
                # SoldToAddress
                csv_line = csv_line + '","'
                # Cost
                csv_line = csv_line + str(move.product_id.list_price or '') + '","' 
                # CustomerNumber
                csv_line = csv_line + str(delivery_record.partner_id.email or '') + '","' 
                # OrderNumber
                csv_line = csv_line + str(delivery_record.origin or '') + '","' 
                # PackageID
                csv_line = csv_line + str(move.tracking_id.name or '') + '",' 
            
                csv += csv_line + '\n'
        
            self.write(cr,uid,ids,{
                'data' : base64.encodestring( csv ),
                'filename': 'DataPac.csv'
            })
        return {
            'type': 'ir.actions.act_window',
            'res_model': 'datapac.create.data.template',
            'view_mode': 'form',
            'view_type': 'form',
            'res_id': this.id,
            'views': [(False, 'form')],
            'target': 'new',
        }

    _columns = {
        'filename': fields.char('File Name', size=32),
        'data': fields.binary('DataPac CSV')
    }

    _defaults = {
    }
datapac_create_data_template()

