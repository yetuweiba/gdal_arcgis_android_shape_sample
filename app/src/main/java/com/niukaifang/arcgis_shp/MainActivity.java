package com.niukaifang.arcgis_shp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.esri.arcgisruntime.data.ShapefileFeatureTable;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.view.MapView;

import org.gdal.gdal.gdal;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Feature;
import org.gdal.ogr.FeatureDefn;
import org.gdal.ogr.FieldDefn;
import org.gdal.ogr.Geometry;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;

import java.io.File;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button mbtnAddShp;
    MapView mMapView;
    ArcGISMap map;

    ShapefileFeatureTable mShapefileFeatureTable = null;
    FeatureLayer mFeatureLayer = null;
    Feature mSelectFeature = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mbtnAddShp = findViewById(R.id.btnAddShp);
        mbtnAddShp.setOnClickListener(this);
        mMapView = findViewById(R.id.mapView);
        map = new ArcGISMap();
        mMapView.setMap(map);
        TianDiTuLayerInfo info = new TianDiTuLayerInfo();
        TianDiTu2000Layer baseLayer = new TianDiTu2000Layer(info.getTianDiTuMLayerInfo(), info.getMFullExtent());
        //baseLayer.setMainURL("http://t0.tianditu.gov.cn/img_c/wmts?service=wmts&request=gettile&version=1.0.0&layer=img&format=tiles&tilematrixset=c&style=default&tk=c177c4c55b5da2d40aec0658a47cd0d2");
//        baseLayer.setMainURL("http://t0.tianditu.gov.cn/img_w/wmts?service=wmts&request=gettile&version=1.0.0&layer=img&format=tiles&tilematrixset=w&style=default&tk=c177c4c55b5da2d40aec0658a47cd0d2");
        baseLayer.setMainURL("http://t0.tianditu.gov.cn/cia_w/wmts?service=wmts&request=gettile&version=1.0.0&layer=cia&format=tiles&tilematrixset=w&style=default&tk=c177c4c55b5da2d40aec0658a47cd0d2");
        baseLayer.setName("baseLayer");
        mMapView.getMap().getBasemap().getBaseLayers().add(GoogleMapLayer.CreateGoogleLayer(GoogleMapLayer.MapType.IMAGE));
        mMapView.getMap().getBasemap().getBaseLayers().add(baseLayer);
        SpatialReference sp = map.getSpatialReference();
        String tt = sp.getWKText();
        int id = sp.getWkid();
        mMapView.getSelectionProperties().setColor(Color.RED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean createShp(String shpPath){
        try{
            ogr.RegisterAll();
            gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "NO");
            gdal.SetConfigOption("SHAPE_ENCODING", "UTF-8");

            String strDriverName = "ESRI Shapefile";
            org.gdal.ogr.Driver oDriver = ogr.GetDriverByName(strDriverName);
            if (oDriver == null) {
                System.out.println(strDriverName + " 驱动不可用");
                return false;
            }

            Vector<String> options = new Vector<>();
            options.add("ENCODING=UTF-8");
            DataSource oDS = oDriver.CreateDataSource(shpPath, options);
            if (oDS == null) {
                System.out.println("创建矢量文件" + shpPath + "失败");
                return false;
            }
            oDS.FlushCache();

            org.gdal.osr.SpatialReference srs = new org.gdal.osr.SpatialReference();
            srs.ImportFromWkt("GEOGCS[\"GCS_China_2000\",DATUM[\"D_China_2000\",SPHEROID[\"China_2000\",6378137,298.257222101]],PRIMEM[\"Greenwich\",0],UNIT[\"DEGREE\",0.0174532925199]]");
            Layer oLayer = oDS.CreateLayer("1", srs, ogr.wkbPolygon, null);
            if (oLayer == null) {
                System.out.println("图层创建失败！\n");
                return false;
            }

            // 下面创建属性表
            // 先创建一个叫FieldID的整型属性
            FieldDefn oFieldID = new FieldDefn("FieldID", ogr.OFTInteger);
            oLayer.CreateField(oFieldID);

            // 再创建一个叫FeatureName的字符型属性，字符长度为50
            FieldDefn oFieldName = new FieldDefn(new String("三角形".getBytes(), "UTF-8"), ogr.OFTString);
            oFieldName.SetWidth(100);
            oLayer.CreateField(oFieldName);

            FeatureDefn oDefn = oLayer.GetLayerDefn();

            // 创建三角形要素
            Feature oFeatureTriangle = new Feature(oDefn);
            oFeatureTriangle.SetField(0, 0);
            oFeatureTriangle.SetField(1, new String("三角形11".getBytes(), "UTF-8"));
            Geometry geomTriangle = Geometry.CreateFromWkt("POLYGON ((0 0,20 0,10 15,0 0))");
            oFeatureTriangle.SetGeometry(geomTriangle);
            oLayer.CreateFeature(oFeatureTriangle);

            // 创建矩形要素
            Feature oFeatureRectangle = new Feature(oDefn);
            oFeatureRectangle.SetField(0, 1);
            oFeatureRectangle.SetField(1, "矩形222");
            Geometry geomRectangle = Geometry.CreateFromWkt("POLYGON ((30 0,60 0,60 30,30 30,30 0))");
            oFeatureRectangle.SetGeometry(geomRectangle);
            oLayer.CreateFeature(oFeatureRectangle);

            // 创建五角形要素
            Feature oFeaturePentagon = new Feature(oDefn);
            oFeaturePentagon.SetField(0, 2);
            oFeaturePentagon.SetField(1, "五角形33");
            Geometry geomPentagon = Geometry.CreateFromWkt("POLYGON ((70 0,85 0,90 15,80 30,65 15,70 0))");
            oFeaturePentagon.SetGeometry(geomPentagon);
            oLayer.CreateFeature(oFeaturePentagon);
            oLayer.SyncToDisk();
            oDS.SyncToDisk();
            oDS.delete();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void loadShp(String shpPath){
        mShapefileFeatureTable = new ShapefileFeatureTable(shpPath);
        mShapefileFeatureTable.loadAsync();
        mShapefileFeatureTable.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                mFeatureLayer = new FeatureLayer(mShapefileFeatureTable);
                mFeatureLayer.setName("operator");
                mFeatureLayer.loadAsync();
                mMapView.getMap().getOperationalLayers().add(mFeatureLayer);
            }
        });
    }

    @Override
    public void onClick(View v){
        if (v.getId() == R.id.btnAddShp){
            File baseFolder = Environment.getExternalStorageDirectory();
            String projectFolderPath = baseFolder.toString() + "/SampleData";
            File projectFolder = new File(projectFolderPath);
            if (!projectFolder.exists()){
                projectFolder.mkdir();
            }
            String shpPath = projectFolder.getPath() + "/test.shp";
            if (createShp(shpPath)){
                loadShp(shpPath);
            }
        }
    }
}
