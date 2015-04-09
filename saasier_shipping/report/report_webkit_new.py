import time
import locale
from report import report_sxw
from osv import osv
import urllib
import base64
class report_webkit_html_new(report_sxw.rml_parse):
    
    _increment = 0
    
    def __init__(self, cr, uid, name, context):
        super(report_webkit_html_new, self).__init__(cr, uid, name, context=context)
        self.localcontext.update({
            'time': time,
            'locale': locale,
            'cr':cr,
            'uid': uid,
            'get_label':self._get_label,
            'get_move_lines':self._get_move_lines,
            'get_move_count':self._get_move_count,
        })
        
        
    def _get_move_lines(self, picking_id, tracking_id):
         
#         move_lines = []
#         if len(picking.move_lines) == 1:
#             return [picking.move_lines]
#         for move in picking.move_lines:
#             move_lines.append(move)
            
#        earlier code 1 label - 1row by 4devnet
#         self.cr.execute("select min(id) from stock_move where picking_id=%s",(picking_id,)) 
#         result = self.cr.fetchall()        
#         move_id = result[0][0] + self._increment
#         self._increment += 1

#        move_obj = self.pool.get('stock.move').browse(self.cr,self.uid,move_id)
            
#        return [move_obj]
      

        move_obj = self.pool.get('stock.move')
        move_ids = move_obj.search(self.cr,self.uid,[('picking_id','=',picking_id),('tracking_id','=',tracking_id)])
        
#         self.cr.execute("select id from stock_move where picking_id=%s and tracking_id=%s",(picking_id,tracking_id,)) 
#         result = self.cr.fetchall()        
#         move_id = result[0][0] 
# 
#         
#         
#         move_obj = self.pool.get('stock.move').browse(self.cr,self.uid,move_id)
            
        return move_obj.browse(self.cr,self.uid,move_ids)
#    
    def _get_move_count(self, picking):
        l = len(picking.move_lines)
        print "======***************==============>",l
        print "===========range(1,16-l)===========>",range(1,16-l)
        return range(1,16-l)
    
    def _get_label(self, picking_id):
        print "====picking_id======>",picking_id
        attach_obj = self.pool.get('ir.attachment')
        attach_ids = attach_obj.search(self.cr,self.uid,[('res_model','=','stock.picking.out'),('res_id','=',picking_id.id)])
        print "attach_ids: ",attach_ids

        if attach_ids:
            attach_obj.browse(self.cr,self.uid,attach_ids[0]).url
#           print "label: ",label
            return attach_obj.browse(self.cr,self.uid,attach_ids)
        return False

        
        
report_sxw.report_sxw('report.webkit.html.new',
                       'stock.picking.out',
                       'shipping_postmaster/report/sale_order.rml',
                       parser=report_webkit_html_new,
                       )
