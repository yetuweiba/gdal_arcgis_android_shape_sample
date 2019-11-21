package com.niukaifang.arcgis_shp;

import com.esri.arcgisruntime.arcgisservices.LevelOfDetail;
import com.esri.arcgisruntime.arcgisservices.TileInfo;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;

import java.util.ArrayList;
import java.util.List;

public class TianDiTuLayerInfo {

    private SpatialReference sp;
    private Envelope fullExtent;
    private Point origin;
    private List<LevelOfDetail> lods;
    private TileInfo tileInfo;

    public TianDiTuLayerInfo(){
        initSpatialReference();
    }

    public SpatialReference getSp() {
        return sp;
    }

    public Point getOrigin() {
        return origin;
    }

    public List<LevelOfDetail> getLods() {
        return lods;
    }

    public Envelope getFullExtent() {
        return fullExtent;
    }

    public TileInfo getTileInfo() {
        return tileInfo;
    }

    double X_MIN_2000 = -180.0;
    double Y_MIN_2000 = -90.0;
    double X_MAX_2000 = 180.0;
    double Y_MAX_2000 = 90.0;

    private static final double X_MIN_MERCATOR = -20037508.3427892;
    private static final double Y_MIN_MERCATOR = -20037508.3427892;
    private static final double X_MAX_MERCATOR = 20037508.3427892;
    private static final double Y_MAX_MERCATOR = 20037508.3427892;

//    private static final double[] SCALES = {
//            2.958293554545656E8, 1.479146777272828E8,
//            7.39573388636414E7, 3.69786694318207E7,
//            1.848933471591035E7, 9244667.357955175,
//            4622333.678977588, 2311166.839488794,
//            1155583.419744397, 577791.7098721985,
//            288895.85493609926, 144447.92746804963,
//            72223.96373402482, 36111.98186701241,
//            18055.990933506204, 9027.995466753102,
//            4513.997733376551, 2256.998866688275,
//            1128.4994333441375
//    };
    private static double[] SCALES = new double[] { 591657527.591555,
        295828763.79577702, 147914381.89788899, 73957190.948944002,
        36978595.474472001, 18489297.737236001, 9244648.8686180003,
        4622324.4343090001, 2311162.217155, 1155581.108577, 577790.554289,
        288895.277144, 144447.638572, 72223.819286, 36111.909643,
        18055.954822, 9027.9774109999998, 4513.9887049999998, 2256.994353,
        1128.4971760000001 };

    private static final double[] RESOLUTIONS_MERCATOR = {156543.03392800014,
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
    private static final SpatialReference SRID_MERCATOR = SpatialReference.create(102100);
    private static final Point ORIGIN_MERCATOR = new Point(-20037508.3427892, 20037508.3427892, SRID_MERCATOR);
    private static final Envelope ENVELOPE_MERCATOR = new Envelope(X_MIN_MERCATOR, Y_MIN_MERCATOR, X_MAX_MERCATOR, Y_MAX_MERCATOR, SRID_MERCATOR);

    static int _tileWidth = 256;
    static int _tileHeight = 256;
    static int _dpi = 96;
    int _GCS2000 = 4490;


    private void initSpatialReference() {
        sp = SpatialReference.create(_GCS2000);
        this.origin = new Point(X_MIN_2000, Y_MAX_2000, sp);
        this.fullExtent = new Envelope(X_MIN_2000, Y_MIN_2000, X_MAX_2000, Y_MAX_2000, sp);
        this.lods = new ArrayList<LevelOfDetail>();
        lods.add(new LevelOfDetail(1, 0.7031249999891485, 2.958293554545656E8));
        lods.add(new LevelOfDetail(2, 0.35156249999999994, 1.479146777272828E8));
        lods.add(new LevelOfDetail(3, 0.17578124999999997, 7.39573388636414E7));
        lods.add(new LevelOfDetail(4, 0.08789062500000014, 3.69786694318207E7));
        lods.add(new LevelOfDetail(5, 0.04394531250000007, 1.848933471591035E7));
        lods.add(new LevelOfDetail(6, 0.021972656250000007, 9244667.357955175));
        lods.add(new LevelOfDetail(7, 0.01098632812500002, 4622333.678977588));
        lods.add(new LevelOfDetail(8, 0.00549316406250001, 2311166.839488794));
        lods.add(new LevelOfDetail(9, 0.0027465820312500017, 1155583.419744397));
        lods.add(new LevelOfDetail(10, 0.0013732910156250009, 577791.7098721985));
        lods.add(new LevelOfDetail(11, 0.000686645507812499, 288895.85493609926));
        lods.add(new LevelOfDetail(12, 0.0003433227539062495, 144447.92746804963));
        lods.add(new LevelOfDetail(13, 0.00017166137695312503, 72223.96373402482));
        lods.add(new LevelOfDetail(14, 0.00008583068847656251, 36111.98186701241));
        lods.add(new LevelOfDetail(15, 0.000042915344238281406, 18055.990933506204));
        lods.add(new LevelOfDetail(16, 0.000021457672119140645, 9027.995466753102));
        lods.add(new LevelOfDetail(17, 0.000010728836059570307, 4513.997733376551));
        lods.add(new LevelOfDetail(18, 0.000005364418029785169, 2256.998866688275));
        tileInfo = getTianDiTuLayerInfo();
    }

    private TileInfo getTianDiTuLayerInfo() {
        TileInfo tileInfo001 = new TileInfo(_dpi, TileInfo.ImageFormat.PNG, lods, origin, sp, _tileHeight, _tileWidth);
        return tileInfo001;
    }

    public TileInfo getTianDiTuMLayerInfo() {
        List<LevelOfDetail> mainLevelOfDetail = new ArrayList<>();
        for (int i = 0; i <= 19; i++) {
            LevelOfDetail item = new LevelOfDetail(i, RESOLUTIONS_MERCATOR[i], SCALES[i]);
            mainLevelOfDetail.add(item);
        }
        TileInfo info = new TileInfo(_dpi, TileInfo.ImageFormat.PNG, mainLevelOfDetail, ORIGIN_MERCATOR, SRID_MERCATOR, _tileHeight, _tileWidth);
        return info;
    }

    public Envelope getMFullExtent(){
        return ENVELOPE_MERCATOR;
    }

}
