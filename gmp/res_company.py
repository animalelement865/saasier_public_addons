from osv import osv,fields
# import time

class res_company(osv.Model):
    _inherit        = "res.company"
    
#     def get_current_date(self,cr,uid,ids,name,args,context=None):
#         res={}
#         for c in self.browse(cr,uid,ids):
#             res[c.id] = time.strftime('%Y-%m-%d')
#         return res
    
    _columns        = {
#                        'system_date'                : fields.function(get_current_date,type='date',string='System Date',store=True),
                       'system_date'                : fields.date('System Date'),
                       'create_initial_audit'       : fields.selection([('on_save','On Save'),
                                                                        ('on_button','On Button')],'Create Initial Audit'),
                       'initial_audit_required'     : fields.boolean('Initial audit required?'),
                       'followup_audit_required'    : fields.boolean('Follow up audits required?'),
                       'coa_verification'           : fields.boolean('COA verification'),
                       'number_of_coa'              : fields.integer('Number of COA Verification Tests'),
                       'sop_ids'                    : fields.many2many('document.page','company_sop_rel','company_id','sop_id','Associated SOPs'),
                       'followup_audit_freq'        : fields.selection([('m','Monthly'),
                                                                        ('q','Quarterly'),
                                                                        ('a','Annually'),
                                                                        ('b','Bi-Annually')],'Follow up audit frequency'),
                       }
    def onchange_audit(self,cr,uid,ids,audit):
        vals={'followup_audit_freq':False}
        if not audit:
            vals['followup_audit_freq']=False
        res={'value':vals}
        return res
    
    def onchange_coa(self,cr,uid,ids,coa):
        vals={'number_of_coa':False}
        if not coa:
            vals['number_of_coa']=0
        res={'value':vals}
        return res
    
    _default        = {
                       'followup_audit_freq'        : lambda *args: 'a',
                       }
res_company()