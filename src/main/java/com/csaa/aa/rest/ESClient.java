package com.csaa.aa.rest;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ESClient {
	private static final Logger logger = LoggerFactory.getLogger(ESClient.class);
	private static TransportClient client;
	
	public static void startClient(){
		logger.debug("Entering ESClient.startClient()");
		logger.debug("Setting cluster name to " + CLUSTER_NAME);
		Settings settings = ImmutableSettings
                .settingsBuilder().
                put("cluster.name", CLUSTER_NAME).
                put("node.master", "false").
                put("node.data", "false").
                put("http.enabled", "false")
                .put("discovery.zen.minimum_master_nodes", 1)
                .put("discovery.zen.ping.multicast.ttl", 4)
                .put("discovery.zen.ping_timeout", 100)
                .put("discovery.zen.fd.ping_timeout", 300)
                .put("discovery.zen.fd.ping_interval", 5)
                .put("discovery.zen.fd.ping_retries", 5)
                .put("client.transport.ping_timeout", "10s")
                .put("multicast.enabled", false)
                //.put("discovery.zen.ping.unicast.hosts", hostName)
                .put("index.merge.async", false)
                .put("transport.netty.worker_count", 1)
                .build();
       
        client = new TransportClient(settings);

        List<InetSocketTransportAddress> transportAddresses = getInetSocketTransportAddresses(MASTER_NODES);

        if (transportAddresses == null)
            throw new NullPointerException(
                    "ElasticSearch End Point are not configured");

        TransportAddress[] taddresses = transportAddresses
                .toArray(new TransportAddress[transportAddresses.size()]);

        for (int index = 0; index < transportAddresses.size(); index++) {
            for (InetSocketTransportAddress address : transportAddresses) {
                taddresses[index] = address;
            }
        }
        client.addTransportAddresses(taddresses);
        
        logger.debug("Exiting ESClient.startClient()");

	}
	private static final String MASTER_NODES = "masternode1.test.com,masternode1.test.com,masternode1.test.com";
	private static final String CLUSTER_NAME = "csaaSearch";

	
	public static List<InetSocketTransportAddress> getInetSocketTransportAddresses( String serverPorts) {
        logger.debug("Value of serverPorts " + serverPorts);
        List<InetSocketTransportAddress> serverTransports = new LinkedList<InetSocketTransportAddress>();

        String[] split = StringUtils.split(serverPorts, ",");

        for (String serverInfo : split) {
            String[] serverTransportsChunks = StringUtils
                    .split(serverInfo, ":");
            String server = serverTransportsChunks[0];
            String port = "";
            if (serverTransportsChunks.length > 1)
                port = serverTransportsChunks[1];
            else
                port = "9300";
            logger.debug("Found node " + server+":"+port);
            InetSocketTransportAddress address = new InetSocketTransportAddress(
                    server, Integer.parseInt(port));
            serverTransports.add(address);

        }
        if (serverTransports.size() == 0)
            return null;
        logger.debug("Returning list of servers " + serverTransports);
        return serverTransports;
    }
	
	public static void stopClient(){
		logger.debug("Entering ESClient.stop()");
		client.close();
		logger.debug("Exiting ESClient.stop()");
		
	}

	public static Client getClient(){
		logger.debug("Entering ESClient.getClient()");
		if(client == null)
			startClient();
		
		logger.debug("Exiting ESClient.getClient()");
		return client;
	}
}
