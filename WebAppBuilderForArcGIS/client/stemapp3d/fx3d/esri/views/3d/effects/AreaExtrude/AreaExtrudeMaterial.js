/**
 * Copyright @ 2017 Esri.
 * All rights reserved under the copyright laws of the United States and applicable international laws, treaties, and conventions.
 */
define(["dojo/text!./AreaExtrudeMaterial.xml","esri/core/declare","esri/views/3d/webgl-engine/lib/GLSLShader","../../webgl-engine-extensions/GLSLProgramExt","../../support/fx3dUtils"],function(e,i,r,t,s){var n=i(null,{declaredClass:"esri.views.3d.effects.AreaExtrude.AreaExtrudeMaterial",constructor:function(e){this._gl=e.gl,this._shaderSnippets=e.shaderSnippets,this._program=null,this._viewingMode=e.viewingMode,this._pushState=e.pushState,this._restoreState=e.restoreState},destroy:function(){this._program&&(this._program.dispose(),delete this._program,this._program=null)},_addDefines:function(e,i){var r="";if(null!=i)if(Array.isArray(i))for(var t=0,s=i.length;t<s;t++){var n=i[t];r+="#define "+n+"\n"}else for(var n in i)r+="#define "+n+"\n";return this._shaderSnippets.defines+"\n"+r+e},loadShaders:function(){if(this._shaderSnippets){null!=this._shaderSnippets.areaExtrudeVS&&null!=this._shaderSnippets.areaExtrudeFS||this._shaderSnippets._parse(e);var i=[];"global"==this._viewingMode?i.push("GLOBAL"):i.push("LOCAL");var s=this._addDefines(this._shaderSnippets.areaExtrudeVS,i),n=new r(35633,s,this._gl),a=new r(35632,this._shaderSnippets.areaExtrudeFS,this._gl);this._program=new t([n,a],this._gl)}return this._initResources()},getProgram:function(){return this._program},_initResources:function(){return!0},bind:function(e,i){this._program.use(),this._program.uniformMatrix4fv("sl",e.proj),this._program.uniformMatrix4fv("os",e.view),this._program.uniform3fv("le",e.camPos),this._program.uniform3fv("mo",e.camPos),this._program.uniform4fv("pi",e.viewport),this._program.uniform3fv("oi",e.lightingData.direction),this._program.uniform4fv("is",e.lightingData.ambient),this._program.uniform4fv("ii",e.lightingData.diffuse),this._program.uniform4fv("ps",e.lightingData.specular),this._oldTex=this._gl.getParameter(32873);var r=i._activeTextureUnit;r>i.parameters.maxVertexTextureImageUnits-1-3&&(console.warn("Many textures are binded now, 3DFx lib may be work abnormally."),r=0),e.ss.bind(r+1),this._program.uniform1i("ss",r+1),e.mm.bind(r+2),this._program.uniform1i("mm",r+2),this._program.uniform2fv("im",e.im),this._program.uniform2fv("ls",[e.ls,e.es]),this._program.uniform2fv("ip",e.ip),this._gl.activeTexture(33984+r+3),this._gl.bindTexture(3553,e.oo),this._program.uniform1i("oo",r+3),this._program.uniform1f("lp",e.lp),this._program.uniform1f("sm",e.sm),this._program.uniform3fv("mi",e.mi),this._program.uniform1f("ll",e.time),this._program.uniform1i("ms",e.reachedRepeatLimit),1!=i._depthTestEnabled&&(this._pushState(["setDepthTestEnabled",i._depthTestEnabled]),i.setDepthTestEnabled(!0)),1!=i._polygonCullingEnabled&&(this._pushState(["setFaceCullingEnabled",i._polygonCullingEnabled]),i.setFaceCullingEnabled(!0)),1!=i._blendEnabled&&(this._pushState(["setBlendingEnabled",i._blendEnabled]),i.setBlendingEnabled(!0))},bindVec3:function(e,i){this._program.uniform3fv(e,i)},release:function(e){this._gl.activeTexture(33984+e._activeTextureUnit+1),this._gl.bindTexture(3553,this._oldTex),this._restoreState(e),this._gl.useProgram(null)}});return n});