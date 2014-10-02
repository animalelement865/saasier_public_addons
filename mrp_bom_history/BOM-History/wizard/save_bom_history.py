from osv import osv,fields
import time
import openerp.addons.decimal_precision as dp

class save_bom_history_line(osv.osv_memory):
    _name       = "save.bom.history.line"
    _columns    = {
                   'name'           : fields.char('Name', size=64),
                   'wizard_id'      : fields.many2one('save.bom.history','Wizard'),
                   'product_id'     : fields.many2one('product.product', 'Product', required=True),
                   'product_qty'    : fields.float('Product Quantity', required=True, digits_compute=dp.get_precision('Product Unit of Measure')),
                   'product_uom'    : fields.many2one('product.uom', 'Product Unit of Measure', required=True, help="Unit of Measure (Unit of Measure) is the unit of measurement for the inventory control"),
                   'date_start'     : fields.date('Valid From', help="Validity of this BoM or component. Keep empty if it's always valid."),
                   'date_stop'      : fields.date('Valid Until', help="Validity of this BoM or component. Keep empty if it's always valid."),
                   }
    _defaults   = {
                   'date_start'     : time.strftime('%Y-%m-%d'),
                   'product_qty'    : 1,
                   }
    
    def onchange_product_id(self, cr, uid, ids, product_id, name, context=None):
        """ Changes UoM and name if product_id changes.
        @param name: Name of the field
        @param product_id: Changed product_id
        @return:  Dictionary of changed values
        """
        if product_id:
            prod = self.pool.get('product.product').browse(cr, uid, product_id, context=context)
            return {'value': {'name': prod.name, 'product_uom': prod.uom_id.id}}
        return {}
    
save_bom_history_line()

class save_bom_history(osv.osv_memory):
    _name       = "save.bom.history"
    _columns    = {
                   'name'           : fields.char('Name'),
                   'cut_off_date'   : fields.date('Cut-off Date'),
                   'new_bom_ids'    : fields.one2many('save.bom.history.line','wizard_id','New Bill of Material'),
                   }
    
    _defaults   = {
                   'name'           : "History",
                   'cut_off_date'   : time.strftime('%Y-%m-%d'), 
                   }
    def save_history(self,cr,uid,ids,context={}):
        for data in self.browse(cr,uid,ids):
            bom = self.pool.get('mrp.bom').browse(cr,uid,context['active_id'])
            #===================================================================
            # 1. 'REMOVE' LAST COMPONENT FROM BOM
            #===================================================================
            write_data = {
                          'active'      : False,
                          'date_stop'   : data.cut_off_date,
                          }
            line_ids=[]
            for bom_lines in bom.bom_lines:
                line_ids.append(bom_lines.id)
            self.pool.get('mrp.bom').write(cr,uid,line_ids,write_data)
            #------------------------------------ REMOVE LAST COMPONENT FROM BOM

            #===================================================================
            # 2. SETUP NEW COMPONENT LIST
            #===================================================================
            for lines in data.new_bom_ids:
                line_data = {
                             'name'             : lines.name,
                             'date_start'       : lines.date_start,
                             'date_stop'        : lines.date_stop,
                             'product_qty'      : lines.product_qty,
                             'product_id'       : lines.product_id and lines.product_id.id or False,
                             'product_uom'      : lines.product_uom and lines.product_uom.id or False,
                             'bom_id'           : bom and bom.id or False,
                             }
                self.pool.get('mrp.bom').create(cr,uid,line_data)
            #------------------------------------------ SETUP NEW COMPONENT LIST
            
            #===================================================================
            # 3. CREATE NEW HISTORY RECORD
            #===================================================================
            history_data        = {'name'   : data.name,
                                   'date'   : data.cut_off_date,
                                   'bom_id' : bom.id or False,
                                   }
            history_line_data   = []
            for bom_lines in bom.bom_lines:
                line_data = {
                             'name'             : bom_lines.name,
                             'code'             : bom_lines.code,
                             'position'         : bom_lines.position,
                             'date_start'       : bom_lines.date_start,
                             'date_stop'        : data.cut_off_date,
                             'sequence'         : bom_lines.sequence,
                             'product_uos_qty'  : bom_lines.product_uos_qty,
                             'product_qty'      : bom_lines.product_qty,
                             'product_id'       : bom_lines.product_id and bom_lines.product_id.id or False,
                             'product_uos'      : bom_lines.product_uos and bom_lines.product_uos.id or False,
                             'product_uom'      : bom_lines.product_uom and bom_lines.product_uom.id or False,
                             'bom_id'           : bom and bom.id or False,
                             }
                history_line_data.append((0,0,line_data))
            history_data['history_ids'] = history_line_data
            self.pool.get('bom.history').create(cr,uid,history_data)
            #----------------------------------------- CREATE NEW HISTORY RECORD
        return True
save_bom_history()