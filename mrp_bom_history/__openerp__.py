{
    "name"          : "BoM History",
    "version"       : "1.0",
    "depends"       : ["mrp","base","product"],
    "author"        : "SaaSier",
    "description"   : """This module is aim to track the changes of Bills of Material.
                         """,
    "website"       : "https://www.saasier.com",
    "category"      : "Manufacture Resource Planning",
    "init_xml"      : [],
    "demo_xml"      : [],
    'test'          : [],
    "update_xml"    : [
                       "wizard/save_bom_history_view.xml",
                       "bom_history_view.xml",
                       ],
    "active"        : False,
    "installable"   : True,
}