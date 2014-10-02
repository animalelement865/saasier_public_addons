import lob
import requests
import json

from osv import osv, fields

class lob_config(osv.osv):
    _name = 'lob.config'
    _columns = {
                    'name'  : fields.char('Account ID', size=128, required=True),
                    'api'   : fields.char('API', size=128, required=True),
    }
    
    def create_bank_account(self, cr, uid, bank_account_id):
        sids = self.search(cr, uid, [])
        assert len(sids)==1
        
        lob_obj = self.browse(cr, uid, sids[0])

        bank_pool = self.pool.get('res.bank')
        bank_ac_pool = self.pool.get('res.partner.bank')
        
        bank_account = bank_ac_pool.browse(cr, uid, bank_account_id)
        
        bank = bank_account.bank
        
        if not bank:
            raise osv.except_osv('Missing bank in bank account', "Please select bank in bank account configuration")

        if not bank.street:
            raise osv.except_osv('Insufficient data', "Please enter street1 in bank address")
        if not bank_account.street:
            raise osv.except_osv('Insufficient data', "Please enter street1 in bank account address")

        if not bank.city:
            raise osv.except_osv('Insufficient data', "Please enter City in bank address")
        if not bank_account.city:
            raise osv.except_osv('Insufficient data', "Please enter City in bank account address")

        if not bank.zip:
            raise osv.except_osv('Insufficient data', "Please enter ZIP code in bank address")
        if not bank_account.zip:
            raise osv.except_osv('Insufficient data', "Please enter ZIP code in bank account address")

        if not bank.country:
            raise osv.except_osv('Insufficient data', "Please enter Country in bank address")
        if not bank_account.country_id:
            raise osv.except_osv('Insufficient data', "Please enter Country in bank account address")
        
        if not bank.state:
            raise osv.except_osv('Insufficient data', "Please enter State in bank address")
        if not bank_account.state:
            raise osv.except_osv('Insufficient data', "Please enter State in bank account address")

        if not bank.routing_no:
            raise osv.except_osv("Missing bank's routing no.", "Please enter routing no. of bank")
        
        lob.api_key = lob_obj.api

        resp =  lob.BankAccount.create(
                                                routing_number = bank.routing_no,
                                                account_number = bank_account.acc_number,
                                                bank_code = bank_account.bank_bic or '',
                                                bank_address={
                                                                "name"            : bank.name,
                                                                "email"           : bank.email or '',
                                                                "phone"           : bank.phone or '',
                                                                "address_line1"   : bank.street or '',
                                                                "address_line2"   : bank.street2 or '',
                                                                "address_city"    : bank.city or '',
                                                                "address_state"   : bank.state.code,
                                                                "address_zip"     : bank.zip or '',
                                                                "address_country" : bank.country.code,
                                                                "object"          : "address"
                                                },
                                                account_address={
                                                                    "name"            : bank_account.owner_name,
                                                                    "email"           : bank_account.partner_id.email or '',
                                                                    "phone"           : bank_account.partner_id.phone or '',
                                                                    "address_line1"   : bank_account.street or '',
                                                                    "address_zip"     : bank_account.zip or '',
                                                                    "address_city"    : bank_account.city or '',
                                                                    "address_state"   : bank_account.state_id.code,
                                                                    "address_country" : bank_account.country_id.code,
                                                                    "object"          : "address"
                                                 },
                                               
                                ).to_dict()
        
        if resp and resp.get('id'):
            bank_ac_pool.write(cr, uid, [bank_account.id], {'lob_id':resp['id']})
        
        if resp and resp.get('account_address'):
            if resp['account_address'].get('id'):
                bank_ac_pool.write(cr, uid, [bank_account.id], {'address_lob_id':resp['account_address']['id']})

        if resp and resp.get('bank_address'):
            if resp['bank_address'].get('id'):
                bank_pool.write(cr, uid, [bank.id], {'lob_id':resp['bank_address']['id']})

        return True
        
lob_config()