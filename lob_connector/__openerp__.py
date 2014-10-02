{
    'name': 'Lob.com Connector',
    'version': '1.0',
    'category': '',
    'sequence': 14,
    'summary': '',
    'description': """
            Openerp - Lob.com Integration
    """,
    'author': 'Zedes',
    'website': 'http://www.zedestech.com',
    'images': [],
    'depends': ['account','sale'],
    'data': [
                'wizard/send_check_view.xml',
                'lob_config_view.xml',
                'account_invoice_view.xml',
                'res_bank_view.xml',
                
                'security/ir.model.access.csv'
     ],
    'demo': [],
    'test': [],
    'installable': True,
    'auto_install': False,
    'application': True,
}