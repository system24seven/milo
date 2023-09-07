package org.eclipse.milo.opcua.stack.core.security;

import java.security.KeyPair;
import java.security.cert.X509Certificate;

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class CertificateStoreTest {

    protected CertificateStore certificateStore;

    @BeforeEach
    void setUpCertificateStore() throws Exception {
        var factory = new TestCertificateFactory();
        KeyPair keyPair = factory.createRsaSha256KeyPair();
        X509Certificate[] certificateChain = factory.createRsaSha256CertificateChain(keyPair);

        certificateStore = newCertificateStore();

        certificateStore.set(
            id("test"),
            new CertificateStore.Entry(keyPair.getPrivate(), certificateChain)
        );
    }

    protected abstract CertificateStore newCertificateStore() throws Exception;

    @Test
    void contains() throws Exception {
        assertTrue(certificateStore.contains(id("test")));
        assertFalse(certificateStore.contains(id("foo")));
    }

    @Test
    void get() throws Exception {
        assertNotNull(certificateStore.get(id("test")));
        assertNull(certificateStore.get(id("foo")));
    }

    @Test
    void remove() throws Exception {
        assertNotNull(certificateStore.remove(id("test")));
        assertNull(certificateStore.remove(id("test")));
        assertFalse(certificateStore.contains(id("test")));
    }

    @Test
    void set() throws Exception {
        var factory = new TestCertificateFactory();
        KeyPair keyPair = factory.createRsaSha256KeyPair();
        X509Certificate[] certificateChain = factory.createRsaSha256CertificateChain(keyPair);

        certificateStore.set(
            id("test2"),
            new CertificateStore.Entry(keyPair.getPrivate(), certificateChain)
        );

        assertTrue(certificateStore.contains(id("test2")));
    }

    private static NodeId id(String id) {
        return new NodeId(2, id);
    }

}