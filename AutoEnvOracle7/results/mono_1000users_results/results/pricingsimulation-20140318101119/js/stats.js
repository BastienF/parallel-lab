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
		total: "2007",
		ok: "1588",
		ko: "419"
	},
	minResponseTime : {
		total: "0",
		ok: "810",
		ko: "0"
	},
	maxResponseTime : {
		total: "62450",
		ok: "62450",
		ko: "60050"
	},
	meanResponseTime : {
		total: "12905",
		ok: "13306",
		ko: "11382"
	},
	standardDeviation : {
		total: "6904",
		ok: "10741",
		ko: "0"
	},
	percentiles1 : {
		total: "40680",
		ok: "40070",
		ko: "60030"
	},
	percentiles2 : {
		total: "60040",
		ok: "40720",
		ko: "60040"
	},
	group1 : {
		name: "t < 800 ms",
		count: 0,
		percentage: 0
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 2,
		percentage: 0
	},
	group3 : {
		name: "t > 1200 ms",
		count: 1586,
		percentage: 79
	},
	group4 : {
		name: "failed",
		count: 419,
		percentage: 20
	},
	meanNumberOfRequestsPerSecond: {
		total: "32",
		ok: "25",
		ko: "7"
	}
}


	}
		},
name: "Global Information",
path: "",
pathFormatted: "missing-name",
stats: {
	numberOfRequests : {
		total: "2007",
		ok: "1588",
		ko: "419"
	},
	minResponseTime : {
		total: "0",
		ok: "810",
		ko: "0"
	},
	maxResponseTime : {
		total: "62450",
		ok: "62450",
		ko: "60050"
	},
	meanResponseTime : {
		total: "12905",
		ok: "13306",
		ko: "11382"
	},
	standardDeviation : {
		total: "6904",
		ok: "10741",
		ko: "0"
	},
	percentiles1 : {
		total: "40680",
		ok: "40070",
		ko: "60030"
	},
	percentiles2 : {
		total: "60040",
		ok: "40720",
		ko: "60040"
	},
	group1 : {
		name: "t < 800 ms",
		count: 0,
		percentage: 0
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 2,
		percentage: 0
	},
	group3 : {
		name: "t > 1200 ms",
		count: 1586,
		percentage: 79
	},
	group4 : {
		name: "failed",
		count: 419,
		percentage: 20
	},
	meanNumberOfRequestsPerSecond: {
		total: "32",
		ok: "25",
		ko: "7"
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
