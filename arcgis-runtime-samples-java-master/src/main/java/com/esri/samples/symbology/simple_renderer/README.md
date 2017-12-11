<h2>Simple Renderer</h2>

<p>Demonstrates how to create a SimpleRenderer and add it to a GraphicsOverlay. Renderers are used to display graphics that don't already have a symbol set. A renderer will not override a symbol that is manually set to a graphic.</p>

<p><img src="SimpleRenderer.png"/></p>

<h2>How to use the sample</h2>

<p>Starts with a predefined SimpleRenderer that sets a cross SimpleMarkerSymbol as a default symbol for graphics.</p>

<h2>How it works</h2>

<p>How to set a default symbol using a <code>SimpleRenderer</code>:</p>

<ol>
 <li>Create a <code>ArcGISMap</code>'s with <code>Basemap</code>.</li>
 <li>Create a <code>GraphicsOverlay</code> and add it to the <code>MapView</code>, <code>MapView.getGraphicsOverlays().add()</code>.</li>
 <li>Add the map to the view, <code>MapView.setMap()</code>.  </li>
 <li>Create a simple renderer using a <code>SimpleMarkerSymbol</code>, <code>SimpleRenderer(Symbol)</code>. </li>
 <li>Lately, set the renderer to graphics overlay using <code>GraphicsOverlay.setRenderer(Renderer)</code>.</li>
</ol>

<h2>Features</h2>

<ul>
 <li>ArcGISMap</li>
 <li>Graphic</li>
 <li>GraphicsOverlay</li>
 <li>MapView</li>
 <li>Point</li>
 <li>SimpleMarkerSymbol</li>
 <li>SimpleMarkerSymbol.Style</li>
 <li>SimpleRenderer</li>
</ul>


