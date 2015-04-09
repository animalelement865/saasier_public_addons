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

class worldship_create_data_template(osv.osv_memory):
    _name = 'worldship.create.data.template'
    _description = 'Create Worldship CSV File'

    def action_create_csv(self, cr, uid, ids, context=None):
        this = self.browse(cr, uid, ids)[0]
        stock_label_obj = self.pool.get('stock.picking.out.label')
        stock_move_obj  = self.pool.get('stock.move')
        stock_label_record = stock_label_obj.browse(cr, uid, context['active_id'], context=context)
        csv = '"OrderId","ShipmentInformation_ServiceType","ShipmentInformation_BillingOption","ShipmentInformation_QvnOption","ShipmentInformation_QvnShipNotification1Option","ShipmentInformation_NotificationRecipient1Type","ShipmentInformation_NotificationRecipient1FaxorEmail","ShipTo_CompanyOrName","ShipTo_StreetAddress","ShipTo_RoomFloorAddress2","ShipTo_City","ShipTo_State","ShipTo_Country","ShipTo_ZipCode","ShipTo_Telephone","ShipTo_ResidentialIndicator","Package_PackageType","Package_Weight","Package_Reference1","Package_Reference2","Package_Reference3","Package_Reference4","Package_Reference5","Package_DeclaredValueOption","Package_DeclaredValueAmount","ShipTo_LocationID"\n'
        for delivery_record in  stock_label_record.picking_ids:
            if delivery_record.shipping_carrier!="UPS WorldShip CSV":
                continue
            cr.execute("""select DISTINCT ON (tracking_id) id, tracking_id, picking_id from stock_move where picking_id=%s""",(delivery_record.id,))
            packs = cr.fetchall()
            for pack in packs:
                csv_line = '"'
                #GET WEIGHT OF PACK
                total_pack_weight = 0
                move_pack_ids = stock_move_obj.search(cr, uid, [('picking_id','=',delivery_record.id),('tracking_id','=',pack[1])])
                for move_record in stock_move_obj.browse(cr, uid, move_pack_ids):
                    total_pack_weight += move_record.override_weight
                # OrderID
                csv_line = csv_line + str(delivery_record.origin or '') + '","'
                # ShipmentInformation_ServiceType
                csv_line = csv_line + str(delivery_record.shipping_service_ups) + '","'
                # ShipmentInformation_BillingOption
                csv_line = csv_line + '","'
                # ShipmentInformation_QvnOption
                csv_line = csv_line + '","'
                # ShipmentInformation_QvnShipNotification1Option
                csv_line = csv_line + '","'
                # ShipmentInformation_NotificationRecipient1Type
                csv_line = csv_line + '","'
                # ShipmentInformation_NotificationRecipient1FaxorEmail
                csv_line = csv_line + str(delivery_record.partner_id.email or '') + '","'
                # ShipTo_CompanyOrName
                csv_line = csv_line + str(delivery_record.partner_id.name) + '","'
                # ShipTo_StreetAddress
                csv_line = csv_line + str(delivery_record.partner_id.street) +'","'
                # ShipTo_RoomFloorAddress2
                csv_line = csv_line + str(delivery_record.partner_id.street2 or '') + '","'
                # ShipTo_City
                csv_line = csv_line + str(delivery_record.partner_id.city or '') + '","'
                # ShipTo_State
                csv_line = csv_line + str(delivery_record.partner_id.state_id.code or '') + '","'
                # ShipTo_Country
                csv_line = csv_line + str(delivery_record.partner_id.country_id.code or '') + '","'
                # ShipTo_ZipCode
                csv_line = csv_line + str(delivery_record.partner_id.zip) + '","'
                # ShipTo_Telephone
                csv_line = csv_line + str(delivery_record.partner_id.phone or '') + '","'
                # ShipTo_ResidentialIndicator
                csv_line = csv_line + '","'
                # Package_PackageType
                csv_line = csv_line + str(delivery_record.shipping_packaging_ups or '') + '","'
                # Package_Weight
                csv_line = csv_line + str(total_pack_weight) + '","'
                # Package_Reference1,2,3,4,5
                csv_line = csv_line + '","'
                csv_line = csv_line + '","'
                csv_line = csv_line + '","'
                csv_line = csv_line + '","'
                csv_line = csv_line + '","'
                # Package_DeclaredValueOption
                csv_line = csv_line + '","'
                # Package_DeclaredValueAmount
                csv_line = csv_line + '","'
                # ShipTo_LocationID
                csv_line = csv_line + '",'
            
                csv += csv_line + '\n'
        
            self.write(cr,uid,ids,{
                'data' : base64.encodestring( csv ),
                'filename': 'WorldShip.csv'
            })
        return {
            'type': 'ir.actions.act_window',
            'res_model': 'worldship.create.data.template',
            'view_mode': 'form',
            'view_type': 'form',
            'res_id': this.id,
            'views': [(False, 'form')],
            'target': 'new',
        }

    _columns = {
        'filename': fields.char('File Name', size=32),
        'data': fields.binary('WorldShip CSV')
    }

    _defaults = {
    }
worldship_create_data_template()

