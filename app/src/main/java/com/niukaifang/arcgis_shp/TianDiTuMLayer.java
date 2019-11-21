package com.niukaifang.arcgis_shp;

import com.esri.arcgisruntime.arcgisservices.TileInfo;
import com.esri.arcgisruntime.data.TileKey;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.layers.ImageTiledLayer;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


//卫星图地址:http://t0.tianditu.com/vec_c/wmts?service=wmts&request=gettile&version=1.0.0&layer=vec&format=tiles&tilematrixset=c
//电子图地址:http://t0.tianditu.com/img_c/wmts?service=wmts&request=gettile&version=1.0.0&layer=img&format=tiles&tilematrixset=c
//标注地址：http://t0.tianditu.com/cia_c/wmts?service=wmts&request=gettile&version=1.0.0&layer=cia&format=tiles&tilematrixset=c

public class TianDiTuMLayer extends ImageTiledLayer {
    private String mainURL = "";

    public TianDiTuMLayer(TileInfo tileInfo, Envelope fullExtent) {
        super(tileInfo, fullExtent);
    }

    public String getMainURL() {
        return mainURL;
    }

    public void setMainURL(String mainURL) {
        this.mainURL = mainURL;
    }

    @Override
    protected byte[] getTile(TileKey tileKey) {
        int level = tileKey.getLevel();
        int col = tileKey.getColumn();
        int row = tileKey.getRow();
        String requestUrl = mainURL + "&TILECOL=" + col + "&TILEROW=" + row + "&TILEMATRIX=" + (level);
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            //获取服务器返回回来的流
            InputStream is = conn.getInputStream();
            return getBytes(is);
        } catch (Exception e) {
            return new byte[0];
        }
    }

    private byte[] getBytes(InputStream is) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        is.close();
        bos.flush();
        byte[] result = bos.toByteArray();
        return result;
    }
}
