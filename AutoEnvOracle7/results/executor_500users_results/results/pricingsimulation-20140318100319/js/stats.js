var stats = {
	type: "GROUP",
contents: {
		
				"price-78a5eb43deef9a7b5b9ce157b9d52ac4": {
		type: "REQUEST",
		name: "price",
path: "price",
pathFormatted: "price-78a5eb43deef9a7b5b9ce157b9d52ac4",
stats: {
	numberOfRequests : {
		total: "500",
		ok: "112",
		ko: "388"
	},
	minResponseTime : {
		total: "0",
		ok: "49480",
		ko: "0"
	},
	maxResponseTime : {
		total: "70320",
		ok: "70320",
		ko: "60040"
	},
	meanResponseTime : {
		total: "12822",
		ok: "52418",
		ko: "1392"
	},
	standardDeviation : {
		total: "0",
		ok: "0",
		ko: "0"
	},
	percentiles1 : {
		total: "52910",
		ok: "58290",
		ko: "0"
	},
	percentiles2 : {
		total: "60030",
		ok: "70280",
		ko: "60030"
	},
	group1 : {
		name: "t < 800 ms",
		count: 0,
		percentage: 0
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 0,
		percentage: 0
	},
	group3 : {
		name: "t > 1200 ms",
		count: 112,
		percentage: 22
	},
	group4 : {
		name: "failed",
		count: 388,
		percentage: 77
	},
	meanNumberOfRequestsPerSecond: {
		total: "7",
		ok: "2",
		ko: "5"
	}
}


	}
		},
name: "Global Information",
path: "",
pathFormatted: "missing-name",
stats: {
	numberOfRequests : {
		total: "500",
		ok: "112",
		ko: "388"
	},
	minResponseTime : {
		total: "0",
		ok: "49480",
		ko: "0"
	},
	maxResponseTime : {
		total: "70320",
		ok: "70320",
		ko: "60040"
	},
	meanResponseTime : {
		total: "12822",
		ok: "52418",
		ko: "1392"
	},
	standardDeviation : {
		total: "0",
		ok: "0",
		ko: "0"
	},
	percentiles1 : {
		total: "52910",
		ok: "58290",
		ko: "0"
	},
	percentiles2 : {
		total: "60030",
		ok: "70280",
		ko: "60030"
	},
	group1 : {
		name: "t < 800 ms",
		count: 0,
		percentage: 0
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 0,
		percentage: 0
	},
	group3 : {
		name: "t > 1200 ms",
		count: 112,
		percentage: 22
	},
	group4 : {
		name: "failed",
		count: 388,
		percentage: 77
	},
	meanNumberOfRequestsPerSecond: {
		total: "7",
		ok: "2",
		ko: "5"
	}
}



}

function fillStats(stat){
    $("#numberOfRequests").append(stat.numberOfRequests.total);
    $("#numberOfRequestsOK").append(stat.numberOfRequests.ok);
    $("#numberOfRequestsKO").append(stat.numberOfRequests.ko);

    $("#minResponseTime").append(stat.minResponseTime.total);
    $("#minResponseTimeOK").append(stat.minResponseTime.ok);
    $("#minResponseTimeKO").append(stat.minResponseTime.ko);

    $("#maxResponseTime").append(stat.maxResponseTime.total);
    $("#maxResponseTimeOK").append(stat.maxResponseTime.ok);
    $("#maxResponseTimeKO").append(stat.maxResponseTime.ko);

    $("#meanResponseTime").append(stat.meanResponseTime.total);
    $("#meanResponseTimeOK").append(stat.meanResponseTime.ok);
    $("#meanResponseTimeKO").append(stat.meanResponseTime.ko);

    $("#standardDeviation").append(stat.standardDeviation.total);
    $("#standardDeviationOK").append(stat.standardDeviation.ok);
    $("#standardDeviationKO").append(stat.standardDeviation.ko);

    $("#percentiles1").append(stat.percentiles1.total);
    $("#percentiles1OK").append(stat.percentiles1.ok);
    $("#percentiles1KO").append(stat.percentiles1.ko);

    $("#percentiles2").append(stat.percentiles2.total);
    $("#percentiles2OK").append(stat.percentiles2.ok);
    $("#percentiles2KO").append(stat.percentiles2.ko);

    $("#meanNumberOfRequestsPerSecond").append(stat.meanNumberOfRequestsPerSecond.total);
    $("#meanNumberOfRequestsPerSecondOK").append(stat.meanNumberOfRequestsPerSecond.ok);
    $("#meanNumberOfRequestsPerSecondKO").append(stat.meanNumberOfRequestsPerSecond.ko);
}
