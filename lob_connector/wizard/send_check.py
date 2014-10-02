from lob_connector import lob
import json
import netsvc
from datetime import datetime

from osv import osv, fields

class send_check(osv.osv_memory):
    _name = 'send.check'
    _columns = {
                'date'          : fields.date('Date'),
                'supplier_id'   : fields.many2one('res.partner', 'Supplier'),
                'bank_id'       : fields.many2one('res.partner.bank', 'Bank'),
                'amount'        : fields.float('Amount', help='Amount of Check '),
                'message'       : fields.char('Message', size=400, help="Max of 400 characters to be included on the top of the check."),
                'memo'          : fields.char('Memo', size=40, help="Max of 40 characters to be included on the memo line of the check."),
                'name'          : fields.char('name', size=40, help="Optional"),
                'number'        : fields.char('Check Number', size=10, help="Checks will default starting at 10000 and increment accordingly."),
    }
    _defaults = {
                 'date' : lambda *a: datetime.today().strftime('%Y-%m-%d'),
    }
    
    def default_get(self, cr, uid, fields, context):
        resp = super(send_check, self).default_get(cr, uid, fields, context)
        invoice = self.pool.get('account.invoice').browse(cr, uid, context.get('active_id'))
        resp['supplier_id'] = invoice.partner_id.id
        resp['amount']      = invoice.residual
        resp['memo']        = invoice.reference
        return resp
    
    def create_check(self, cr, uid, ids, context=None):
        sobj         = self.browse(cr, uid, ids, context=None)[0]
        lob_pool     = self.pool.get('lob.config')
        inv_pool = self.pool.get('account.invoice')
        invoice_id    = context.get('active_id')
        
        supplier = sobj.supplier_id
        
        if (not supplier.street) or (not supplier.city) or (not supplier.state_id) or (not supplier.country_id):
            raise osv.except_osv('Insufficient data', "Please enter address of supplier (street, city, zip, state and country are required)")
        
        
        sobj = self.browse(cr, uid, ids[0])
        if not sobj.bank_id.lob_id:
            lob_pool.create_bank_account(cr, uid, sobj.bank_id.id)
            sobj = self.browse(cr, uid, ids[0])

        bank_acc = sobj.bank_id

        if not bank_acc.journal_id:
            raise osv.except_osv('Insufficient data', "Please select Account Journal in bank account")

        lob_ids = lob_pool.search(cr, uid, [])
        assert len(lob_ids)==1
        
        lob_obj = lob_pool.browse(cr, uid, lob_ids[0])
        lob.api_key = lob_obj.api
        
        resp = False
        try:
            resp = lob.Check.create(
                                    name         = sobj.name or '',
                                    check_number = sobj.number or '',
                                    message      =  sobj.message or '',
                                    memo         =  sobj.memo or '',
                                    amount       = sobj.amount,
                                    bank_account =  bank_acc.lob_id,
                                    to = {
                                            "name"            : sobj.supplier_id.name,
                                            "address_line1"   : sobj.supplier_id.street ,
                                            "address_line2"   : sobj.supplier_id.street2 or "",
                                            "address_city"    : sobj.supplier_id.city or "",
                                            "address_state"   : sobj.supplier_id.state_id.code or '',
                                            "address_country" : sobj.supplier_id.country_id.code or '',
                                            "address_zip"     : sobj.supplier_id.zip or '',
                                    },
                                ).to_dict()
                                
        except Exception, e:
            raise osv.except_osv('Error Raised by Lob.com', str(e))

                
        if resp and resp.get('id'):
            inv_pool.write(cr, uid, [invoice_id], {'lob_check_id' : resp['id']})
            self.process_payment(cr, uid, ids, context)
            user = self.pool.get('res.users').browse(cr, uid, uid)
            vals = {
                        'body': u'<br/><br/><p><b>Check of Amount %s Sent Via Lob.com, Check #: %s</b></p><br/><br/>' %(resp.get('amount'),resp.get('check_number')), 
                        'model': 'account.invoice', 
                        'res_id': invoice_id, 
                        'subtype_id': False, 
                        'author_id': user.partner_id.id, 
                        'type': 'comment', 
                    }        
            self.pool.get('mail.message').create(cr,uid,vals)
        return True
    
    def process_payment(self, cr, uid, ids, context=None):
        wf_service      = netsvc.LocalService("workflow")
        
        invoice_id = context.get('active_id')
        invoice = self.pool.get('account.invoice').browse(cr, uid, invoice_id)
        sobj = self.browse(cr, uid, ids[0])
        
        journal = sobj.bank_id.journal_id
        
        move_line_id = self.pool.get('account.move.line').search(cr, uid, [('invoice','=',invoice_id)])
        
        ctx = { 
                'default_partner_id'  : sobj.supplier_id.id, 
                'default_amount'      : sobj.amount, 
                'default_name'        : sobj.memo,  
                'close_after_process' : True, 
                'invoice_type'        : invoice.type,  
                'invoice_id'          : invoice.id, 
                'default_type'        : 'payment', 
                'type'                : 'payment',
                'default_type'        : 'payment',
        }
                
        payment_vals = {
                        'date'           : sobj.date,  
                        'account_id'     : journal.default_debit_account_id.id, 
                        'amount'         : sobj.amount, 
                        'journal_id'     : journal.id,
                        'partner_id'     : sobj.supplier_id.id, 
                        'payment_option' : 'without_writeoff', 
                        'type'           : 'payment', 
                        'comment'        : 'Write-Off',
                        'line_dr_ids'    : [[0, False, {
                                                            'account_id'          : sobj.supplier_id.property_account_payable.id, 
                                                            'amount'              : sobj.amount,
                                                            'amount_original'     : invoice.amount_total, 
                                                            'amount_unreconciled' : invoice.residual,
                                                            'move_line_id'        : move_line_id and move_line_id[0] or False, 
                                                            'reconcile'           : True, 
                                                            'type'                : 'dr'
                                           }]],
                }
                
        payment_id = self.pool.get('account.voucher').create(cr, uid, payment_vals, ctx)
        wf_service.trg_validate(uid, 'account.voucher', payment_id, 'proforma_voucher', cr)
                            
        return

send_check()