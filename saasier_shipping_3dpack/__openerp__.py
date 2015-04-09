{
    "name" : "saasier_shipping_3dpack",
    "version" : "1.0",
  
    "author" : "Chris Jones",
    "description": """3dbinpacking.com Integration Module""",
    "website" : "www.saasier.com",
    "category" : "tools",
    "depends": ['base','saasier_shipping',],
    "data" : [
       "stock_view.xml",
       "product_view.xml",
       "security/ir.model.access.csv",
    ],
    "installable": True,
    "auto_install": False,
    "application": True,
}