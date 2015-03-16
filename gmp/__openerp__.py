{
	'name': 'GMP',
	'version': '1.0',
	'description': """
		GMP Compliance Module
	""",
	'author': 'Chris Jones',
	'website': 'http://www.saasier.com',
	'depends': ['base','base_setup', 'product', 'stock', 'mgmtsystem', 'mgmtsystem_audit','mgmtsystem_review','mro'], 
	'data': [
			'res_config_view.xml',
			'gmp_view.xml',
			'res_partner_view.xml',
			'res_company_view.xml',
			'maintenance_view.xml',
			'data/scheduler.xml',
			'security/ir.model.access.csv',
	],
	'demo': [],
	'installable': True,
	'auto_install': False,
}


