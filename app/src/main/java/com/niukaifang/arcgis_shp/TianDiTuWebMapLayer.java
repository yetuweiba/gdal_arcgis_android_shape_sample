package com.niukaifang.arcgis_shp;


import com.esri.arcgisruntime.arcgisservices.LevelOfDetail;
import com.esri.arcgisruntime.arcgisservices.TileInfo;
import com.esri.arcgisruntime.data.TileKey;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.WebTiledLayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TianDiTuWebMapLayer {

    private static final int DPI = 96;
    private static final int minZoomLevel = 0;
    private static final int maxZoomLevel = 18;
    private static final int tileWidth = 256;
    private static final int tileHeight = 256;

    // 枚举
    public enum MapType {
        VECTOR,        //矢量标注地图
        IMAGE,        //影像地图
        ROAD,        //道路标注图层
    }


    private MapType mMapType;

    private static final List<String> SubDomain = Arrays.asList(new String[]{"t0", "t1", "t2", "t3", "t4"});
    //102100是esri内部使用id 与epsg：3857 是对应的
    private static final SpatialReference SRID_MERCATOR = SpatialReference.create(102113);
    private static final Point ORIGIN_MERCATOR = new Point(-20037508.3427892, 20037508.3427892, SRID_MERCATOR);
    private static final Envelope ENVELOPE_MERCATOR = new Envelope(-22041257.773878,
            -32673939.6727517, 22041257.773878, 20851350.0432886, SRID_MERCATOR);

    private static double[] SCALES = new double[] { 591657527.591555,
            295828763.79577702, 147914381.89788899, 73957190.948944002,
            36978595.474472001, 18489297.737236001, 9244648.8686180003,
            4622324.4343090001, 2311162.217155, 1155581.108577, 577790.554289,
            288895.277144, 144447.638572, 72223.819286, 36111.909643,
            18055.954822, 9027.9774109999998, 4513.9887049999998, 2256.994353,
            1128.4971760000001 };

    private static double[] iRes = new double[] { 156543.03392800014,
            78271.516963999937, 39135.758482000092, 19567.879240999919,
            9783.9396204999593, 4891.9698102499797, 2445.9849051249898,
            1222.9924525624949, 611.49622628138, 305.748113140558,
            152.874056570411, 76.4370282850732, 38.2185141425366,
            19.1092570712683, 9.55462853563415, 4.7773142679493699,
            2.3886571339746849, 1.1943285668550503, 0.59716428355981721,
            0.29858214164761665 };

    private static final double[] RESOLUTIONS_MERCATOR = {
            78271.51696402048, 39135.75848201024,
            19567.87924100512, 9783.93962050256,
            4891.96981025128, 2445.98490512564,
            1222.99245256282, 611.49622628141,
            305.748113140705, 152.8740565703525,
            76.43702828517625, 38.21851414258813,
            19.109257071294063, 9.554628535647032,
            4.777314267823516, 2.388657133911758,
            1.194328566955879, 0.5971642834779395,
            0.298582141738970};

    public static WebTiledLayer CreateTianDiTuLayer() {
        WebTiledLayer webTiledLayer = null;
        String mainUrl = "";
        String mainName = "TianDiTu";
        TileInfo mainTileInfo = null;
        Envelope mainEnvelope = null;
        try {
            mainUrl = "http://{subDomain}.tianditu.gov.cn/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={level}&TILEROW={col}&TILECOL={row}&tk=c177c4c55b5da2d40aec0658a47cd0d2";
            List<LevelOfDetail> mainLevelOfDetail = new ArrayList<LevelOfDetail>();
            Point mainOrigin = null;
            for (int i = minZoomLevel; i <= maxZoomLevel; i++) {
                LevelOfDetail item = new LevelOfDetail(i, iRes[i], RESOLUTIONS_MERCATOR[i]);
                mainLevelOfDetail.add(item);
            }
            mainEnvelope = ENVELOPE_MERCATOR;
            mainOrigin = ORIGIN_MERCATOR;

            mainTileInfo = new TileInfo(
                    DPI,
                    TileInfo.ImageFormat.PNG24,
                    mainLevelOfDetail,
                    mainOrigin,
                    mainOrigin.getSpatialReference(),
                    tileHeight,
                    tileWidth
            );
            webTiledLayer = new WebTiledLayer(
                    mainUrl,
                    SubDomain,
                    mainTileInfo,
                    mainEnvelope);
            webTiledLayer.setName(mainName);
            webTiledLayer.loadAsync();
        } catch (Exception e) {
            e.getCause();
        }
        return webTiledLayer;
    }
}
