

{
    'name': 'Incoming Shipment',
    'version': '1.1',
    'author': 'Jaydev Bhagat',
    'summary': 'QR Barcode Functionality',
    'description' : """
    QR Barcode Functionality
    """,
    'website': 'http://www.4devnet.com',
    'depends': ['stock','mrp'],
    'category': 'Warehouse Management',
    'sequence': 16,
    'data': [

        'stock_custom_view.xml',

    ],
    'installable': True,
    'application': True,
    'auto_install': False,

}

# vim:expandtab:smartindent:tabstop=4:softtabstop=4:shiftwidth=4:
