// All material copyright ESRI, All Rights Reserved, unless otherwise specified.
// See http://js.arcgis.com/3.15/esri/copyright.txt and http://www.arcgis.com/apps/webappbuilder/copyright.txt for details.
//>>built
require({cache:{"url:builder/plugins/map-config/CustomizeScale.html":'\x3cdiv\x3e\r\n  \x3cdiv class\x3d"scale-input-div" data-dojo-attach-point\x3d"scaleInputDiv"\x3e\r\n    \x3cdiv class\x3d"add-btn jimu-float-leading" data-dojo-attach-point\x3d"btnAdd" data-dojo-attach-event\x3d"onclick:_onBtnAddClicked"\x3e+\x3c/div\x3e\r\n  \x3c/div\x3e\r\n  \x3cdiv class\x3d"operations-div"\x3e\r\n    \x3cdiv class\x3d"jimu-float-leading operation-btn delete-btn" data-dojo-attach-point\x3d"btnDelete" data-dojo-attach-event\x3d"onclick:_onBtnDeleteClicked"\x3e\r\n      \x3cspan class\x3d"jimu-float-leading icon"\x3e\x3c/span\x3e\r\n      \x3cspan class\x3d"jimu-float-leading label"\x3e${nls.deleteTip}\x3c/span\x3e\r\n    \x3c/div\x3e\r\n    \x3cdiv class\x3d"jimu-float-leading operation-btn reset-btn" data-dojo-attach-event\x3d"onclick:_onBtnResetClicked"\x3e\r\n      \x3cspan class\x3d"jimu-float-leading icon"\x3e\x3c/span\x3e\r\n      \x3cspan class\x3d"jimu-float-leading label"\x3e${nls.resetTip}\x3c/span\x3e\r\n    \x3c/div\x3e\r\n  \x3c/div\x3e\r\n  \x3cdiv class\x3d"list" data-dojo-attach-point\x3d"listNode" data-dojo-attach-event\x3d"onclick:_onLodListClicked"\x3e\x3c/div\x3e\r\n\x3c/div\x3e'}});
define("dojo/_base/declare dijit/_WidgetBase dijit/_TemplatedMixin dijit/_WidgetsInTemplateMixin dojo/text!./CustomizeScale.html dojo/query dojo/_base/lang dojo/_base/array dojo/_base/html jimu/utils jimu/dijit/Message dijit/form/NumberTextBox".split(" "),function(m,n,p,q,r,g,h,k,d,t,u,v){return m([n,p,q],{templateString:r,baseClass:"mapconfig-customize-scale",map:null,postCreate:function(){this.inherited(arguments);this._initSelf()},_initSelf:function(){this.scaleNumberBox=new v({placeholder:this.nls.scale,
intermediateChanges:!0,trim:!0,constraints:{min:1,pattern:"###############0.################"},style:"width:550px;height:100%;"});d.addClass(this.scaleNumberBox.domNode,"jimu-float-leading");this.scaleNumberBox.placeAt(this.scaleInputDiv,"first");var a=this.getLodsFromMap();this._resetLodListByLods(a)},getLodsFromMap:function(){var a=[];this.map._params&&this.map._params.lods&&(a=k.map(this.map._params.lods,h.hitch(this,function(a){return a.toJson()})));return a},_getLodsFromBasemapLayer:function(){var a=
[],b=this.map.getLayer(this.map.layerIds[0]);b&&b.tileInfo&&b.tileInfo.lods&&(a=k.map(b.tileInfo.lods,h.hitch(this,function(a){return{level:a.level,resolution:a.resolution,scale:a.scale}})));return a},getLodsFromListNode:function(){for(var a=this._getLodItems(),b=[],d=null,e=0;e<a.length;e++)d=a[e].lod,b.push({level:d.level,scale:d.scale,resolution:d.resolution});return b},_resetLodListByLods:function(a){d.empty(this.listNode);k.forEach(a,h.hitch(this,function(a){a=this._createLodItem(a);d.place(a,
this.listNode)}))},_createLodItem:function(a){var b=d.create("div",{innerHTML:t.localizeNumber(a.scale),"class":"lod-item"});b.lod=a;return b},_getLodItems:function(){return g(".lod-item",this.listNode)},_getSelectedLodItem:function(){var a=null,b=g(".selected",this.listNode);0<b.length&&(a=b[0]);return a},_selectLodItem:function(a){this._removeSelectedClass();d.addClass(a,"selected");d.addClass(this.btnDelete,"enabled-operation");a.previousSibling||d.setStyle(a,"borderTop","0")},_removeSelectedClass:function(){g(".lod-item",
this.listNode).removeClass("selected");d.removeClass(this.btnDelete,"enabled-operation")},_onLodListClicked:function(a){a=a.target||a.srcElement;d.hasClass(a,"lod-item")&&this._selectLodItem(a)},_onBtnAddClicked:function(){if(this.scaleNumberBox.validate()){var a=this.scaleNumberBox.get("value");this.scaleNumberBox.set("displayedValue","");var b=this._getLodItems();if(0!==b.length&&!k.some(b,h.hitch(this,function(b){return b.lod.scale===a}))){var g=b.length-1,e=b[0].lod,f=b[g].lod,c=null,c={level:null,
resolution:e.resolution/e.scale*a,scale:a};if(c.scale>e.scale)c.level=e.level-1,c=this._createLodItem(c),d.place(c,this.listNode,"first");else if(c.scale<f.scale)c.level=f.level+1,c=this._createLodItem(c),d.place(c,this.listNode);else for(var l=null,e=null,f=g;1<=f;f--)if(l=b[f],e=b[f-1],c.scale>l.lod.scale&&c.scale<e.lod.scale){c.level=l.lod.level;c=this._createLodItem(c);d.place(c,e,"after");for(c=f;c<=g;c++)b[c].lod.level+=1;break}}}},_onBtnDeleteClicked:function(){var a=this._getSelectedLodItem();
if(a)if(1===this.getLodsFromListNode().length)new u({message:this.nls.atLeastOneScale});else{var b=a;if(!a.previousSibling)for(;a.nextSibling;)a=a.nextSibling,--a.lod.level;d.destroy(b);this._removeSelectedClass()}},_onBtnResetClicked:function(){this._removeSelectedClass();var a=this._getLodsFromBasemapLayer();this._resetLodListByLods(a)}})});