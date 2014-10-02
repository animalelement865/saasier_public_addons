from osv import osv, fields

class account_invoice(osv.osv):
    _inherit = 'account.invoice'
    _columns = {
                'lob_check_id'  : fields.char('Lob Check ID', size=128),
    }
    
account_invoice()