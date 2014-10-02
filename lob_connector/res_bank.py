from osv import osv, fields

class res_partner_bank(osv.osv):
    _inherit = 'res.partner.bank'
    _columns = {
                    'lob_id'    : fields.char('Lob ID', size=128),
                    'address_lob_id': fields.char('Lob ID', size=128),
    }
    
    def recreate_bank_on_lob(self, cr, uid, ids, context=None):
        return self.pool.get('lob.config').create_bank_account(cr, uid, ids[0])
    
res_partner_bank()

class res_bank(osv.osv):
    _inherit = 'res.bank'
    _columns = {
                    'routing_no'    : fields.char('Routing No.', size=16),
                    'lob_id'        : fields.char('Lob ID', size=128),
    }
res_bank()

class account_voucher(osv.osv):
    _inherit = 'account.voucher'
    _columns = {
                
                
    }
    
    def create(self, cr, uid, vals, context=None):
        print "Create called", vals, context
        return super(account_voucher, self).create(cr, uid, vals, context)
        
account_voucher()