<h1>Open Map (URL)</h1>

<p>Demonstrates how to show a web map using its URL.</p>

<p><img src="OpenMapURL.png"/></p>

<h2>How to use the sample</h2>

<p>A web map can be selected from the drop-down list. On selection the web map displays in the MapView.</p>

<h2>How it works</h2>

<p>To open a web map:</p>

<ol>
    <li>Create a <code>Portal</code> from the ArcGIS URL http://www.arcgis.com.</li>
    <li>Create a <code>PortalItem</code> using the Portal and the web map ID: <code>new PortalItem(portal, ID)</code>.</li>
    <li>Create a <code>ArcGISMap</code> using the portal item.</li>
    <li>Set the map to the <code>MapView</code>.</li>
</ol>

<h2>Features</h2>

<ul>
    <li>ArcGISMap</li>
    <li>MapView</li>
    <li>Portal</li>
    <li>PortalItem</li>
</ul>


