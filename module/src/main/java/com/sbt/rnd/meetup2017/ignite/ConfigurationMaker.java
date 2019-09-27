package com.sbt.rnd.meetup2017.ignite;

import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.*;
import org.apache.ignite.cache.affinity.rendezvous.RendezvousAffinityFunction;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.hibernate.ogm.datastore.ignite.IgniteConfigurationBuilder;

import java.util.*;

public class ConfigurationMaker implements IgniteConfigurationBuilder {
    @Override
    public IgniteConfiguration build() {
        return GridConfig.igniteConfiguration(false).setCacheConfiguration(createCacheConfig());
    }

    private CacheConfiguration[] createCacheConfig() {
        List<CacheConfiguration<?, ?>> cacheConfig = new ArrayList<>();

        addCacheConfigs(cacheConfig);

        return cacheConfig.toArray(new CacheConfiguration[cacheConfig.size()]);
    }

    public void addCacheConfigs(List<CacheConfiguration<?, ?>> config) {
        config.add(
                createCacheConfig("Currency", CacheMode.REPLICATED)
                        .withKeyType(Long.class)
                        .appendIndex("id", Long.class)
                        .appendField("code", String.class)
                        .appendField("intCode", Integer.class)
                        .appendField("name", String.class)
                        .appendField("default", Boolean.class)
                        .build()
        );
    }

    public TestCacheConfigBuilder createCacheConfig(String name, CacheMode cacheMode) {
        return new TestCacheConfigBuilder(name, cacheMode);
    }

    public class TestCacheConfigBuilder {

        private CacheConfiguration cacheConfig;
        private QueryEntity queryEntity;
        private Map<String, QueryIndex> indexes;
        private Class<?> keyType = String.class;
        private String keyTypeName = null;
        private boolean forceQueryEntity = false;

        public TestCacheConfigBuilder(String name, CacheMode cacheMode) {
            cacheConfig = new CacheConfiguration<Object, Object>();
            cacheConfig.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
            cacheConfig.setCacheMode(cacheMode);
            cacheConfig.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
            cacheConfig.setBackups(0);
            cacheConfig.setAffinity(new RendezvousAffinityFunction(false, 2));
            cacheConfig.setCopyOnRead(false);
            cacheConfig.setName(name);

            queryEntity = new QueryEntity();
            queryEntity.setValueType(name);
            indexes = new HashMap<>();
        }

        public TestCacheConfigBuilder appendField(String fieldName, Class<?> fieldType) {
            queryEntity.addQueryField(fieldName, fieldType.getName(), null);
            return this;
        }

        public TestCacheConfigBuilder appendIndex(String fieldName, Class<?> fieldType) {
            queryEntity.addQueryField(fieldName, fieldType.getName(), null);
            indexes.put(fieldName, new QueryIndex(fieldName, QueryIndexType.SORTED));
            return this;
        }

        public TestCacheConfigBuilder withForceQueryEntity() {
            forceQueryEntity = true;
            return this;
        }

        public TestCacheConfigBuilder withKeyType(Class<?> keyClass) {
            this.keyType = keyClass;
            forceQueryEntity = true;
            return this;
        }

        public TestCacheConfigBuilder withKeyType(String keyType) {
            this.keyTypeName = keyType;
            forceQueryEntity = true;
            return this;
        }

        public CacheConfiguration<String, BinaryObject> build() {
            if (forceQueryEntity || !indexes.isEmpty() || !queryEntity.getFields().isEmpty()) {
                queryEntity.setKeyType(keyTypeName != null ? keyTypeName : keyType.getName());
                queryEntity.setIndexes(new HashSet<QueryIndex>(indexes.values()));
                cacheConfig.setQueryEntities(Collections.singletonList(queryEntity));
            }
            if (keyTypeName == null) {
                cacheConfig.setTypes(keyType, Object.class);
            }
            return cacheConfig;
        }
    }
}