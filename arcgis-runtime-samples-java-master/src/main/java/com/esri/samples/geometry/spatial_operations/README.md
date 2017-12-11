<h1>Spatial Operations</h1>

<p>Demonstrates how to use the GeometryEngine to perform geometry operations between overlapping polygons in a 
GraphicsOverlay.</p>

<p><img src="SpatialOperations.png"/></p>

<h2>How to use the sample</h2>

<p>The sample provides a drop down on the top, where you can select a geometry operation. When you choose a geometry 
operation, the application performs this operation between the overlapping polygons and applies the result to the 
geometries.</p>

<h2>How it works</h2>

<p>To find the union, difference, intersection, or symmetric difference between <code>Polygon</code>s:</p>

<ol>
    <li>Create a <code>GraphicsOverlay</code> and add it to the <code>MapView</code>.</li>
    <li>Define a <code>PointCollection</code> of each <code>Geometry</code>.</li>
    <li>Add the overlapping polygons to the graphics overlay.</li>
    <li>Determine spatial relationships between polygons, e.g. union, difference, etc, by using the appropriate operation <code>GeometryEngine.operation(polygon.getGeometry(), polygon.getGeometry())</code></li>
</ol>

<h2>Features</h2>
<ul>
    <li>Geometry</li>
    <li>Graphic</li>
    <li>GraphicsOverlay</li>
    <li>MapView</li>
    <li>Point</li>
    <li>PointCollection</li>
    <li>SimpleLineSymbol</li>
    <li>SimpleFillSymbol</li>
</ul>
