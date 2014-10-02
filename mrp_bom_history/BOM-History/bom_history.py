from osv import osv, fields
import openerp.addons.decimal_precision as dp

class bom_history_line(osv.osv):
    _name       = "bom.history.line"
    _columns    = {
                   'name'           : fields.char('Name', size=64),
                   'code'           : fields.char('Reference', size=16),
                   'position'       : fields.char('Internal Reference', size=64, help="Reference to a position in an external plan."),
                   'date_start'     : fields.date('Valid From', help="Validity of this BoM or component. Keep empty if it's always valid."),
                   'date_stop'      : fields.date('Valid Until', help="Validity of this BoM or component. Keep empty if it's always valid."),
                   'sequence'       : fields.integer('Sequence', help="Gives the sequence order when displaying a list of bills of material."),
                   'product_uos_qty': fields.float('Product UOS Qty'),
                   'product_qty'    : fields.float('Product Quantity', required=True, digits_compute=dp.get_precision('Product Unit of Measure')),
                   'history_id'     : fields.many2one('bom.history','History ID',ondelete='cascade'),
                   'product_id'     : fields.many2one('product.product', 'Product', required=True),
                   'product_uos'    : fields.many2one('product.uom', 'Product UOS', help="Product UOS (Unit of Sale) is the unit of measurement for the invoicing and promotion of stock."),
                   'product_uom'    : fields.many2one('product.uom', 'Product Unit of Measure', required=True, help="Unit of Measure (Unit of Measure) is the unit of measurement for the inventory control"),
                   'bom_id'         : fields.many2one('mrp.bom', 'Parent BoM', ondelete='cascade', select=True),
                   'routing_id'     : fields.many2one('mrp.routing', 'Routing', help="The list of operations (list of work centers) to produce the finished product. The routing is mainly used to compute work center costs during operations and to plan future loads on work centers based on production planning."),
                   'company_id'     : fields.many2one('res.company','Company',required=True),
                   'property_ids'   : fields.many2many('mrp.property', 'mrp_bom_property_rel', 'bom_id','property_id', 'Properties'),
                   }
    _defaults   = {
                   'product_qty'    : lambda *a: 1.0,
                   'company_id'     : lambda self,cr,uid,c: self.pool.get('res.company')._company_default_get(cr, uid, 'mrp.bom', context=c),
                   }
bom_history_line()

class bom_history(osv.osv):
    _name       = "bom.history"
    _columns    = {
                   'name'           : fields.char('Name',size=100),
                   'date'           : fields.date('Date'),
                   'create_uid'     : fields.many2one('res.users','Responsible'),
                   'create_date'    : fields.datetime('Create Date'),
                   'bom_id'         : fields.many2one('mrp.bom','BoM',ondelete="cascade"),
                   'history_ids'    : fields.one2many('bom.history.line','history_id','BoM'),
                   }
    _order      = "create_date desc"
bom_history()

class mrp_bom(osv.osv):
    _inherit    = "mrp.bom"
    _columns    = {
                   'history_ids'    : fields.one2many('bom.history','bom_id','History'),
                   }
    
mrp_bom()