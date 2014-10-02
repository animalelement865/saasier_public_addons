{
    "name"          : "BoM History",
    "version"       : "1.0",
    "depends"       : ["mrp","base","product"],
    "author"        : "Togar Hutabarat",
    "description"   : """This module is aim to track the changes of Bills of Material.
                         Devoted to http://www.animalelement.com""",
    "website"       : "https://www.odesk.com/users/~014ecb73724f396338",
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