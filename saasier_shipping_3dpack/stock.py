# -*- coding: utf-8 -*-
##############################################################################
#
#    OpenERP, Open Source Management Solution
#    Copyright (C) 2004-2009 Tiny SPRL (<http://tiny.be>).
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
from datetime import datetime, date
import time
from openerp.osv import osv, fields
from openerp import netsvc
from tools.translate import _
import logging
import httplib
import urllib
import json
import operator

#Initiate 3dbinpacking.com connection
conn = httplib.HTTPConnection(host='eu.api.3dbinpacking.com', port=80)
bin_packing_data = {"username":"saasier",
                    "api_key":"c04d53b3ffa88887980174d4bbbe3585",
                    "params": {"images_width":100,
                               "images_height":100,
                               "images_source":"file",
                               "images_sbs":0,
                               "images_complete":0,
                               "images_separated":0}
}

class stock_picking_out(osv.osv):
    _inherit = "stock.picking.out"
    _columns = {
    }

    def call_bin_packing_api(self, cr, uid, ids, context=None):
        return self.pool.get('stock.picking').call_bin_packing_api(cr, uid, ids, context=context)
        
stock_picking_out()


class stock_picking(osv.osv):
    _name = "stock.picking"
    _inherit = "stock.picking"

    _columns = {
    }

    def call_bin_packing_api(self, cr, uid, ids, context=None):
        #Initiating Objects and Data
        box_obj = self.pool.get('box.setup')
        move_obj = self.pool.get('stock.move')
        tracking_obj = self.pool.get('stock.tracking')

        all_box_ids = box_obj.search(cr, uid, [('name','!=','')])
        bin_packing_data['bins'] = []
        bin_packing_data['items'] = []
        picking_rec = self.browse(cr, uid, ids[0], context)
        #Get all available boxes, and format them to be sent to 3dbinpacking
        for box_rec in box_obj.browse(cr, uid, all_box_ids,context):
            bin = {"w":box_rec.width,
                   "h":box_rec.height,
                   "d":box_rec.length,
                   "id":box_rec.id,
                   "max_wg":box_rec.maximum_weight,
            }
            bin_packing_data['bins'].append(bin)
        #Get all available unpacked products data, and format them to be sent to 3dbinpacking
        move_ids_3_del = []
        for move_rec in picking_rec.move_lines:
            if move_rec.tracking_id:
                continue
            else:
                item = {"w":move_rec.product_id.width,
                        "h":move_rec.product_id.height,
                        "d":move_rec.product_id.length,
                        "q":move_rec.product_qty,
                        "vr":1,
                        "id":move_rec.id,
                        "wg":move_rec.product_id.weight_net,
                }
                move_ids_3_del.append(move_rec.id)
                bin_packing_data['items'].append(item)

        params = urllib.urlencode({'query':json.dumps(bin_packing_data)})
        headers = {"Content-type": "application/x-www-form-urlencoded", "Accept": "text/plain"}
        conn.request("POST", "/packer/packIntoMany", params, headers)
        content = conn.getresponse().read()
        conn.close()
        
        
        #Create Move Lines of Packed Products
        for bin in eval(content)['response']['bins_packed']:
            move_lines_data = {}
            bin['items'].sort(key=operator.itemgetter('id'))
            # assert False, bin['items']    
            for item in bin['items']:
                if not move_lines_data.has_key(item['id']):
                    move_lines_data[item['id']] = 1
                else:
                    move_lines_data[item['id']] += 1
                    
            #Duplicate the move line and write qty and pack information
            tracking_id = tracking_obj.create(cr, uid, {'active':1,'box_setup_id':bin['bin_data']['id']})
            for item_id in move_lines_data.keys():
                move_cp_id = move_obj.copy(cr, uid, item_id)
                move_obj.write(cr, uid, move_cp_id, {'product_qty': move_lines_data[item_id], 'tracking_id' : tracking_id}) 
                
        #Create Move Lines of Not Packed Products
        for not_packed_item in eval(content)['response']['not_packed_items']:
            move_lines_data = {}                    
            #Duplicate the line and Copy
            move_cp_id = move_obj.copy(cr, uid, not_packed_item['id'])
            move_obj.write(cr, uid, move_cp_id, {'product_qty': not_packed_item['q']})

        #Delete all modified lines
        move_obj.unlink(cr, uid, move_ids_3_del)
        return True
        
stock_picking()