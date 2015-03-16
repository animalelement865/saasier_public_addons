from osv import osv, fields
from openerp.tools.translate import _
import time
from time import mktime
from datetime import datetime
from dateutil.relativedelta import relativedelta

qlf_status = [('c','Certified'),
              ('n','Non-Certified'),
              ('d','De-Certified'),
              ('i','Certifying')]
settings = ['initial_audit_required',
            'followup_audit_required',
            'coa_verification',
            'number_of_coa',
            'sop_ids',
            'followup_audit_freq']

class res_partner(osv.Model):
    _inherit        = "res.partner"
    
    def get_settings(self,cr,uid,ids,context=None):
        user = self.pool.get('res.users').browse(cr, uid, uid, context=context)
        res = self.pool.get('res.company').read(cr,uid,user.company_id.id,settings)
        del res['id']
        return res
    def _get_state(self, cr, uid, ids, name, args, context=None):
        settings=self.get_settings(cr,uid,ids)
        res = {}
        for partner in self.browse(cr, uid, ids):
            if partner.initial_audit and partner.followup_audit and partner.coa_verification:
                self.write(cr,uid,ids,{'been_certified':True})
                res[partner.id] = 'c'
            else:
                if partner.been_certified:
                    res[partner.id] = 'd'
                else:
                    if partner.number_verified_coa!=0 or partner.initial_audit:
                        res[partner.id] = 'i'
                    else:
                        res[partner.id] = 'n'
            if not partner.initial_audit and not partner.followup_audit and not partner.coa_verification and partner.number_verified_coa==0:
                res[partner.id] = 'n'
        return res
    
    #===========================================================================
    # GET PARTNER ID THAT NEED TO BE UPDATED, TRIGGER BY....
    #===========================================================================
    def get_partner_trigger_by_company(self, cr, uid, ids, context=None):
        res = self.pool.get('res.partner').search(cr,uid,[('supplier','=',True),('gmp_vendor','=',True)])
        return res
    def get_partner_trigger_by_audit(self, cr, uid, ids, context=None):
        res=[]
        audits = self.pool.get('mgmtsystem.audit').browse(cr,uid,ids)
        for audit in audits:
            res.append(audit.res_partner_id.id)
        return res
    def get_partner_trigger_by_coa(self, cr, uid, ids, context=None):
        res=[]
        coas = self.pool.get('gmp.coa').browse(cr,uid,ids)
        for coa in coas:
            res.append(coa.partner_id.id)
        return res
        
    #===========================================================================
    # SET WHETHER INITIAL AUDIT IS CHECKED OR NOT
    #===========================================================================
    def set_initial_audit(self, cr, uid, ids, name, args, context=None):
        res={}
        user = self.pool.get('res.users').browse(cr, uid, uid, context=context)
        if user.company_id.initial_audit_required:
            for v in self.browse(cr,uid,ids):
                audit_ids = self.pool.get('mgmtsystem.audit').search(cr,uid,[('res_partner_id','=',v.id),('state','=','pass')])
                if audit_ids:
                    res[v.id]=True
                else:
                    res[v.id]=False
        else:
            for v in self.browse(cr,uid,ids):
                res[v.id]=True
        return res
    
    #===========================================================================
    # SET WHETHER FOLLOW-UP AUDIT IS CHECKED OR NOT
    #===========================================================================
    def set_followup_audit(self, cr, uid, ids, name, args, context=None):
        res={}
        user = self.pool.get('res.users').browse(cr, uid, uid, context=context)
        if user.company_id.followup_audit_required:
            for v in self.browse(cr,uid,ids):
                audit_ids = self.pool.get('mgmtsystem.audit').search(cr,uid,[('res_partner_id','=',v.id),('state','=','pass')],order='date desc')
                if audit_ids:
                    audit=self.pool.get('mgmtsystem.audit').browse(cr,uid,audit_ids[0])
                    now=time.strftime('%Y-%m-%d')
                    
                    audit_date = audit.date
                    audit_date_strct = time.strptime(audit_date, '%Y-%m-%d %H:%M:%S')
                    audit_mktime = datetime.fromtimestamp(mktime(audit_date_strct))
                    
                    audit_date_obj = audit_mktime.date()
                    if user.company_id.followup_audit_freq=='m':
                        next_audit_date = audit_date_obj+relativedelta(months=1)
                    elif user.company_id.followup_audit_freq=='q':
                        next_audit_date = audit_date_obj+relativedelta(months=3)
                    elif user.company_id.followup_audit_freq=='a':
                        next_audit_date = audit_date_obj+relativedelta(years=1)
                    elif user.company_id.followup_audit_freq=='b':
                        next_audit_date = audit_date_obj+relativedelta(months=6)
                    
                    next_audit_date_str = next_audit_date.strftime('%Y-%m-%d')
                    
                    if now <= next_audit_date_str:
                        res[v.id]=True
                    else:
                        res[v.id]=False
                else:
                    res[v.id]=False
        else:
            for v in self.browse(cr,uid,ids):
                res[v.id]=True
        return res
    
    #===========================================================================
    # SET WHETHER COA VERIFICATION IS CHECKED OR NOT
    #===========================================================================
    def set_coa_verification(self, cr, uid, ids, name, args, context=None):
        res={}
        user = self.pool.get('res.users').browse(cr, uid, uid, context=context)
        if user.company_id.coa_verification:
            for v in self.browse(cr,uid,ids):
                coa_ids = self.pool.get('gmp.coa').search(cr,uid,[('partner_id','=',v.id),('state','=','approved')])
                if len(coa_ids)>=user.company_id.number_of_coa:
                    res[v.id]=True
                else:
                    res[v.id]=False
        else:
            for v in self.browse(cr,uid,ids):
                res[v.id]=True
        return res
    
    #===========================================================================
    # SET NUMBER OF VERIFIED COA
    #===========================================================================
    def set_number_verified_coa(self, cr, uid, ids, name, args, context=None):
        res={}
        for v in self.browse(cr,uid,ids):
            coa_ids = self.pool.get('gmp.coa').search(cr,uid,[('partner_id','=',v.id),('state','=','approved')])
            res[v.id]=len(coa_ids)
        return res
    
    #===========================================================================
    # SET NUMBER OF PASSED AUDIT
    #===========================================================================
    def set_number_passed_audit(self, cr, uid, ids, name, args, context=None):
        res={}
        for v in self.browse(cr,uid,ids):
            audit_ids = self.pool.get('mgmtsystem.audit').search(cr,uid,[('partner_id','=',v.id),('state','=','pass')])
            res[v.id]=len(audit_ids)
        return res

    _columns        = {
                       'gmp_vendor'                 : fields.boolean('GMP Vendor',help='By checking this box, system will automatically generate initial audit for this vendor.'),
                       'coa_ids'                    : fields.one2many('gmp.coa','partner_id','COA'),
                       'audit_ids'                  : fields.one2many('mgmtsystem.audit','res_partner_id','Audit'),
                       'been_certified'             : fields.boolean('Been Certified'),
                       
                       'initial_audit_required'     : fields.related('company_id','initial_audit_required',type='boolean',string='Initial audit required?',store=True),
                       'followup_audit_required'    : fields.related('company_id','followup_audit_required',type='boolean',string='Follow up audits required?',store=True),
                       'coa_verification_required'  : fields.related('company_id','coa_verification',type='boolean',string='COA verification',store=True),
                       'number_of_coa'              : fields.related('company_id','number_of_coa',type='boolean',string='Number of COA Verification Tests',store=True),
                       'followup_audit_freq'        : fields.related('company_id','followup_audit_freq',type='boolean',string='Number of COA Verification Tests',store=True),
                       
                       'qlf_status'                 : fields.function(_get_state, type='selection', selection=qlf_status, 
                                                                        string='Status',help='''This field is not editable.
                                                                        Vendor qualification status will change automatically 
                                                                        based to GMP settings and based on any records in system'''),
                       'initial_audit'              : fields.function(set_initial_audit,type='boolean',string='Initial Audit',
                                                      store={
                                                             'res.company': (get_partner_trigger_by_company,['initial_audit_required'],None, 10),
                                                             'mgmtsystem.audit': (get_partner_trigger_by_audit,None, 10),
                                                             }
                                                      ),
                       'followup_audit'             : fields.function(set_followup_audit,type='boolean',string='Follow-up Audit',
                                                      store={
                                                             'res.company': (get_partner_trigger_by_company,['followup_audit_required','system_date','followup_audit_freq'],None, 10),
                                                             'mgmtsystem.audit': (get_partner_trigger_by_audit,None, 10),
                                                             }
                                                      ),
                       'coa_verification'           : fields.function(set_coa_verification,type='boolean',string='COA Verification',
                                                      store={
                                                             'res.company': (get_partner_trigger_by_company,['coa_verification'],None, 10),
                                                             'gmp.coa': (get_partner_trigger_by_coa,None, 10),
                                                             }
                                                      ),
                       'number_verified_coa'        : fields.function(set_number_verified_coa,type='integer',string='Number of Verified COAs',
                                                      store={
                                                             'res.company': (get_partner_trigger_by_company,['number_of_coa'],None, 10),
                                                             'gmp.coa': (get_partner_trigger_by_coa,None, 10),
                                                             }
                                                      ),
                       }
    def write(self,cr,uid,ids,vals,context=None):
        user = self.pool.get('res.users').browse(cr,uid,uid)			
	
        for partner in self.browse(cr,uid,ids):			
            if 'gmp_vendor' in vals.keys() and vals['gmp_vendor']:
                audit_ids = self.pool.get('mgmtsystem.audit').search(cr,uid,[('res_partner_id','=',partner.id),('name','like','Initial')])
		
                if audit_ids:
                    continue
                else:
                    #print "Write method called......=============================================" , user.company_id.sop_ids
                    sop_ids = []
                    for sop in user.company_id.sop_ids:
                        sop_ids.append((4,sop.id))
                    audit = {'name':'%s Initial Audit'%partner.name,
                             'res_partner_id':partner.id,
                             'sop_ids':sop_ids,
                             'user_id':user.id,
                             'date':time.strftime('%Y-%m-%d %H:%M:%S')}
                    #print "Write method called......=============================================" , audit
                    self.pool.get('mgmtsystem.audit').create(cr,uid,audit,context)
                    #print "After Create called this is the print..============================================="                     
        return super(res_partner,self).write(cr,uid,ids,vals,context)
    
    def create(self,cr,uid,vals,context=None):
        user = self.pool.get('res.users').browse(cr,uid,uid)
        if 'gmp_vendor' in vals.keys() and vals['gmp_vendor']:
            sop_ids = []
            for sop in user.company_id.sop_ids:
                sop_ids.append((4,sop.id))
            audit = {'name':'%s Initial Audit'%vals['name'],
                     'sop_ids':sop_ids,
                     'date':time.strftime('%Y-%m-%d %H:%M:%S')}
            vals['audit_ids']= [(0,0,audit)]
        return super(res_partner,self).create(cr,uid,vals,context)
res_partner()
