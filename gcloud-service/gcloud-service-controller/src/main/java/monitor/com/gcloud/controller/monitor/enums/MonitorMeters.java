package com.gcloud.controller.monitor.enums;

public enum MonitorMeters {
	VM_CPU_UTIL("vm.cpu_util"), 
	VM_MEMORY_UTIL("vm.memory_util"), 
	VM_DISK_READ_RATE("vm.disk_read_rate"), 
	VM_DISK_WRITE_RATE("vm.disk_write_rate"), 
	VM_NIC_RX_RATE("vm.nic_rx_rate"), 
	VM_NIC_TX_RATE("vm.nic_tx_rate"), 
	HOST_CPU_UTIL("host.cpu_util"), 
	HOST_MEMORY_UTIL("host.memory_util"), 
	HOST_DISK_READ_RATE("host.disk_read_rate"), 
	HOST_DISK_WRITE_RATE("host.disk_write_rate"), 
	HOST_NIC_RX_RATE("host.nic_rx_rate"), 
	HOST_NIC_TX_RATE("host.nic_tx_rate"), 
	HOST_CPU_LOAD_TIME("host.cpu_load_time"), 
	HOST_ROOT_DF_UTIL("host.root_df_util"), 
	HOST_SYSTEM_BOARD_TEMP("host.system_board_temp"), 
	HOST_CONNECTION("host.connection"), 
	HOST_NETCARD_CONNECTION("host.netcard_connection");

	private String value;

	private MonitorMeters(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}