package com.xyoye.sardine.impl.handler;

import com.xyoye.sardine.impl.SardineException;
import com.xyoye.sardine.model.Multistatus;
import com.xyoye.sardine.util.SardineUtil;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by guillaume on 20/11/2017.
 */

public class MultiStatusResponseHandler extends ValidatingResponseHandler<Multistatus> {
    @Override
    public Multistatus handleResponse(Response response) throws IOException {
        super.validateResponse(response);

        ResponseBody body = response.body();
        if (body == null) {
            throw new SardineException("No entity found in response", response.code(), response.message());
        }

        return getMultistatus(body.byteStream());
    }

    /**
     * Helper method for getting the Multistatus response processor.
     *
     * @param stream The input to read the status
     * @return Multistatus element parsed from the stream
     * @throws IOException When there is a JAXB error
     */
    protected Multistatus getMultistatus(InputStream stream) throws IOException {
        return SardineUtil.unmarshal(Multistatus.class, stream);
    }
}
