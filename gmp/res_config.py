from openerp.osv import fields, osv

class gmp_config_settings(osv.osv_memory):
    _name       = "gmp.config.settings"
    _inherit    = "res.config.settings"
    _columns    = {
                   'has_default_company'    : fields.boolean('Has default company', readonly=True),
                   'company_id'             : fields.many2one('res.company', 'Company', required=True),
                   'sop_ids'                : fields.many2many('document.page','gmpconf_sop_rel','sop_id','gmpconf_id','Associated SOPs'),
                   'initial_audit_required'     : fields.boolean('Initial audit required?'),
                   'followup_audit_required'    : fields.boolean('Follow up audits required?'),
                   'coa_verification'           : fields.boolean('COA verification'),
                   'number_of_coa'              : fields.integer('Number of COA Verification Tests'),
                   'followup_audit_freq'        : fields.selection([('m','Monthly'),
                                                                    ('q','Quarterly'),
                                                                    ('a','Annually'),
                                                                    ('b','Bi-Annually')],'Follow up audit frequency'),
#                    'initial_audit_required' : fields.related('company_id','initial_audit_required',type='boolean',string='Initial audit required?'),
#                    'followup_audit_required': fields.related('company_id','followup_audit_required',type='boolean',string='Follow up audits required?'),
#                    'coa_verification'       : fields.related('company_id','coa_verification',type='boolean',string='COA verification'),
#                    'number_of_coa'          : fields.related('company_id','number_of_coa',type='integer',string='Number of COA Verification Tests'),
#                    'followup_audit_freq'    : fields.related('company_id','followup_audit_freq',type='selection',selection=[('m', 'Monthly'),('q', 'Quarterly'),('a', 'Annually'),('b', 'Bi-Annually')],string='Follow up audit frequency'),
                   }
    
    def get_default_gmp(self, cr, uid, fields, context=None):
        user = self.pool.get('res.users').browse(cr, uid, uid, context=context)
        
        sop_list = []
        for sop in user.company_id.sop_ids:
            sop_list.append(sop.id)
        
        return {
            'sop_ids': sop_list,
            'initial_audit_required': user.company_id.initial_audit_required,
            'followup_audit_required': user.company_id.followup_audit_required,
            'coa_verification': user.company_id.coa_verification,
            'number_of_coa': user.company_id.number_of_coa,
            'followup_audit_freq': user.company_id.followup_audit_freq,
        }

    def set_default_gmp(self, cr, uid, ids, context=None):
        config = self.browse(cr, uid, ids[0], context)
        user = self.pool.get('res.users').browse(cr, uid, uid, context)
        
        current_list=[]
        for current in user.company_id.sop_ids:
            current_list.append((3,current.id))
            user.company_id.write({'sop_ids':current_list})
        user.company_id.write({'sop_ids':(6,0,[])})
        
        sop_list = []
        for sop in config.sop_ids:
            sop_list.append((4,sop.id))
        
        user.company_id.write({
            'sop_ids': sop_list,
            'initial_audit_required': config.initial_audit_required,
            'followup_audit_required': config.followup_audit_required,
            'coa_verification': config.coa_verification,
            'number_of_coa': config.number_of_coa,
            'followup_audit_freq': config.followup_audit_freq,
        })

    def _default_has_default_company(self, cr, uid, context=None):
        count = self.pool.get('res.company').search_count(cr, uid, [], context=context)
        return bool(count == 1)
    def _default_company(self, cr, uid, context=None):
        user = self.pool.get('res.users').browse(cr, uid, uid, context=context)
        return user.company_id.id
    
    _defaults = {
        'company_id': _default_company,
        'has_default_company': _default_has_default_company,
    }