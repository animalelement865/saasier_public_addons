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
from datetime import datetime
from openerp import pooler
from openerp.report.interface import report_rml
from openerp.report.interface import toxml
#import qrcode
class report_custom(report_rml):   
     
     def create_xml(self, cr, uid, ids, datas, context=None):        
         config = ''
         
         for ids_1 in ids:             
             picking_ids = pooler.get_pool(cr.dbname).get('stock.picking.in').browse(cr, uid, ids_1)
             move_id = pooler.get_pool(cr.dbname).get('stock.move').search(cr,uid,[('picking_id','=',picking_ids.id)])
             if move_id:
                 for move in move_id: 
                     move_obj = pooler.get_pool(cr.dbname).get('stock.move').browse(cr,uid,move)
                     if move_obj.qr_img:                                                 
                         name = move_obj.product_id.name_template or ''
                         qr =  move_obj.prodlot_id.ref or ''
                         serial = move_obj.prodlot_id.name or ''              
                         qty = move_obj.product_qty or ''              
                         dest = move_obj.location_dest_id.name or ''
                         origin = move_obj.origin or ''
                         date = move_obj.date[:10] or ''
                         config += """
                             <lot-line type='fields' name='id'>
                                 <name>""" + name + """</name>                         
                                 <qr>""" + qr + """</qr>
                                 <serial>""" + serial + """</serial>
                                 <qty>""" + str(qty) + """</qty>
                                 <dest>""" + str(dest) + """</dest>
                                 <origin>""" + str(origin) + """</origin>
                                 <date>""" + str(date) + """</date>
                              </lot-line> 
                               """
                     else:
                        name = move_obj.product_id.name_template or ''                     
                        serial = move_obj.prodlot_id.name or ''              
                        qty = move_obj.product_qty or ''              
                        dest = move_obj.location_dest_id.name or ''
                        origin = move_obj.origin or ''
                        date = move_obj.date[:10] or ''
                        config += """
                            <lot-line type='fields' name='id'>
                                <name>""" + name + """</name>                        
                                <serial>""" + serial + """</serial>
                                <qty>""" + str(qty) + """</qty>
                                <dest>""" + str(dest) + """</dest>
                                <origin>""" + str(origin) + """</origin>
                                <date>""" + str(date) + """</date>
                            </lot-line>
                            """                                    
         xml = """<?xml version="1.0" encoding="utf-8"?>
                         <lots>""" + config + """</lots>"""    
                                            
         return xml

report_custom('report.picking.incoming','stock.picking.in','','addons/gmp_qrcode_report/report/print_label.xsl')
