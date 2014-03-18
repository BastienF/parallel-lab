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
		total: "901",
		ok: "864",
		ko: "37"
	},
	minResponseTime : {
		total: "0",
		ok: "110",
		ko: "0"
	},
	maxResponseTime : {
		total: "44890",
		ok: "44890",
		ko: "34290"
	},
	meanResponseTime : {
		total: "19788",
		ok: "20278",
		ko: "8339"
	},
	standardDeviation : {
		total: "14578",
		ok: "14371",
		ko: "14710"
	},
	percentiles1 : {
		total: "43620",
		ok: "43650",
		ko: "34290"
	},
	percentiles2 : {
		total: "44690",
		ok: "44690",
		ko: "34290"
	},
	group1 : {
		name: "t < 800 ms",
		count: 20,
		percentage: 2
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 11,
		percentage: 1
	},
	group3 : {
		name: "t > 1200 ms",
		count: 833,
		percentage: 92
	},
	group4 : {
		name: "failed",
		count: 37,
		percentage: 4
	},
	meanNumberOfRequestsPerSecond: {
		total: "15",
		ok: "14",
		ko: "1"
	}
}


	}
		},
name: "Global Information",
path: "",
pathFormatted: "missing-name",
stats: {
	numberOfRequests : {
		total: "901",
		ok: "864",
		ko: "37"
	},
	minResponseTime : {
		total: "0",
		ok: "110",
		ko: "0"
	},
	maxResponseTime : {
		total: "44890",
		ok: "44890",
		ko: "34290"
	},
	meanResponseTime : {
		total: "19788",
		ok: "20278",
		ko: "8339"
	},
	standardDeviation : {
		total: "14578",
		ok: "14371",
		ko: "14710"
	},
	percentiles1 : {
		total: "43620",
		ok: "43650",
		ko: "34290"
	},
	percentiles2 : {
		total: "44690",
		ok: "44690",
		ko: "34290"
	},
	group1 : {
		name: "t < 800 ms",
		count: 20,
		percentage: 2
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 11,
		percentage: 1
	},
	group3 : {
		name: "t > 1200 ms",
		count: 833,
		percentage: 92
	},
	group4 : {
		name: "failed",
		count: 37,
		percentage: 4
	},
	meanNumberOfRequestsPerSecond: {
		total: "15",
		ok: "14",
		ko: "1"
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
