package cn.aposoft.tutorial.http.impl.execchain;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.conn.EofSensorInputStream;
import org.apache.http.conn.EofSensorWatcher;
import org.apache.http.entity.HttpEntityWrapper;

/**
 * A wrapper class for {@link HttpEntity} enclosed in a response message.
 *
 * @since 4.3
 */
class ResponseEntityProxy extends HttpEntityWrapper implements EofSensorWatcher {

    private final ConnectionHolder connHolder;

    public static void enchance(final HttpResponse response, final ConnectionHolder connHolder) {
        final HttpEntity entity = response.getEntity();
        if (entity != null && entity.isStreaming() && connHolder != null) {
            response.setEntity(new ResponseEntityProxy(entity, connHolder));
        }
    }

    ResponseEntityProxy(final HttpEntity entity, final ConnectionHolder connHolder) {
        super(entity);
        this.connHolder = connHolder;
    }

    private void cleanup() throws IOException {
        if (this.connHolder != null) {
            this.connHolder.close();
        }
    }

    private void abortConnection() throws IOException {
        if (this.connHolder != null) {
            this.connHolder.abortConnection();
        }
    }

    public void releaseConnection() throws IOException {
        if (this.connHolder != null) {
            this.connHolder.releaseConnection();
        }
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public InputStream getContent() throws IOException {
        return new EofSensorInputStream(this.wrappedEntity.getContent(), this);
    }

    @Deprecated
    @Override
    public void consumeContent() throws IOException {
        releaseConnection();
    }

    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        try {
            if (outstream != null) {
                this.wrappedEntity.writeTo(outstream);
            }
            releaseConnection();
        } catch (final IOException ex) {
            abortConnection();
            throw ex;
        } catch (final RuntimeException ex) {
            abortConnection();
            throw ex;
        } finally {
            cleanup();
        }
    }

    @Override
    public boolean eofDetected(final InputStream wrapped) throws IOException {
        try {
            // there may be some cleanup required, such as
            // reading trailers after the response body:
            if (wrapped != null) {
                wrapped.close();
            }
            releaseConnection();
        } catch (final IOException ex) {
            abortConnection();
            throw ex;
        } catch (final RuntimeException ex) {
            abortConnection();
            throw ex;
        } finally {
            cleanup();
        }
        return false;
    }

    @Override
    public boolean streamClosed(final InputStream wrapped) throws IOException {
        try {
            final boolean open = connHolder != null && !connHolder.isReleased();
            // this assumes that closing the stream will
            // consume the remainder of the response body:
            try {
                if (wrapped != null) {
                    wrapped.close();
                }
                releaseConnection();
            } catch (final SocketException ex) {
                if (open) {
                    throw ex;
                }
            }
        } catch (final IOException ex) {
            abortConnection();
            throw ex;
        } catch (final RuntimeException ex) {
            abortConnection();
            throw ex;
        } finally {
            cleanup();
        }
        return false;
    }

    @Override
    public boolean streamAbort(final InputStream wrapped) throws IOException {
        cleanup();
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ResponseEntityProxy{");
        sb.append(wrappedEntity);
        sb.append('}');
        return sb.toString();
    }

}
