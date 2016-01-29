
package com.bboniao.vsa.io.f4v.box;

import com.bboniao.vsa.io.f4v.Payload;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class STCO implements Payload {

    private static final Logger logger = LoggerFactory.getLogger(STCO.class);

    private final boolean co64;
    private List<Long> offsets;

    public STCO(ByteBuf in) {
        this(in, false);
    }

    public STCO(ByteBuf in, boolean co64) {
        this.co64 = co64;
        read(in);
    }

    public void setOffsets(List<Long> offsets) {
        this.offsets = offsets;
    }

    public List<Long> getOffsets() {
        return offsets;
    }


    public void read(ByteBuf in) {
        in.readInt(); // UI8 version + UI24 flags
        final int count = in.readInt();
        logger.debug("no of chunk offsets: {}", count);
        offsets = new ArrayList<Long>(count);
        for (int i = 0; i < count; i++) {
            final Long offset = co64 ? in.readLong() : in.readInt();
            // logger.debug("#{} offset: {}", new Object[]{i, offset});
            offsets.add(offset);
        }
    }


    public ByteBuf write() {
        ByteBuf out = Unpooled.buffer();
        out.writeInt(0); // UI8 version + UI24 flags        
        out.writeInt(offsets.size());
        for (Long offset : offsets) {
            if(co64) {
                out.writeLong(offset);
            } else {
                out.writeInt(offset.intValue());
            }
        }
        return out;
    }
    
}
