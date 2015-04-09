# -*- coding: utf-8 -*-
##############################################################################
#
#    TeckZilla Software Solutions and Services
#    Copyright (C) 2012-2013 TeckZilla-OpenERP Experts(<http://www.teckzilla.net>).
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
import base64
from PyPDF2 import PdfFileMerger, PdfFileReader

class merge_pdf(osv.osv_memory):
    _name = "merge.pdf"
  
    
    def merge_pdf(self, cr, uid, ids, context):
        print context['active_ids']
        stock_pick_obj=self.pool.get('stock.picking.out')
        attach_object=self.pool.get('ir.attachment')
        count=0
        merge_file = "/var/www/merge.pdf"
        if os.path.exists(merge_file ):
            os.remove(merge_file )
        merger = PdfFileMerger()
        for ative_id in context['active_ids']:
            print ative_id
            count+=1
            stock_data=stock_pick_obj.browse(cr,uid,ative_id )
            attach_id=attach_object.search(cr,uid,[('res_id','=',ative_id ),('res_model','=',context['active_model'])])
            attachment_data=attach_object.browse(cr,uid,attach_id)
           # print attachment_data[0].res_id
            
            child_pdf_file= "/var/www/master_"+stock_data.carrier_tracking_ref+".pdf"

            merger.append(PdfFileReader(file(child_pdf_file, 'rb')))
        merger.write(merge_file)
            
        url = "http://localhost/merge.pdf"
        return {
                'type': 'ir.actions.act_url',
                'url': url,
                'target': 'new'
            }

merge_pdf()
