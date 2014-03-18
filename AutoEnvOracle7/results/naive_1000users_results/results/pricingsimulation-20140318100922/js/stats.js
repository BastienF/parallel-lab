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
		total: "1773",
		ok: "1268",
		ko: "505"
	},
	minResponseTime : {
		total: "0",
		ok: "80",
		ko: "0"
	},
	maxResponseTime : {
		total: "60050",
		ok: "56380",
		ko: "60050"
	},
	meanResponseTime : {
		total: "15542",
		ok: "15338",
		ko: "16054"
	},
	standardDeviation : {
		total: "5303",
		ok: "9119",
		ko: "0"
	},
	percentiles1 : {
		total: "46420",
		ok: "44900",
		ko: "60020"
	},
	percentiles2 : {
		total: "60020",
		ok: "46440",
		ko: "60040"
	},
	group1 : {
		name: "t < 800 ms",
		count: 9,
		percentage: 0
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 3,
		percentage: 0
	},
	group3 : {
		name: "t > 1200 ms",
		count: 1256,
		percentage: 70
	},
	group4 : {
		name: "failed",
		count: 505,
		percentage: 28
	},
	meanNumberOfRequestsPerSecond: {
		total: "29",
		ok: "21",
		ko: "8"
	}
}


	}
		},
name: "Global Information",
path: "",
pathFormatted: "missing-name",
stats: {
	numberOfRequests : {
		total: "1773",
		ok: "1268",
		ko: "505"
	},
	minResponseTime : {
		total: "0",
		ok: "80",
		ko: "0"
	},
	maxResponseTime : {
		total: "60050",
		ok: "56380",
		ko: "60050"
	},
	meanResponseTime : {
		total: "15542",
		ok: "15338",
		ko: "16054"
	},
	standardDeviation : {
		total: "5303",
		ok: "9119",
		ko: "0"
	},
	percentiles1 : {
		total: "46420",
		ok: "44900",
		ko: "60020"
	},
	percentiles2 : {
		total: "60020",
		ok: "46440",
		ko: "60040"
	},
	group1 : {
		name: "t < 800 ms",
		count: 9,
		percentage: 0
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 3,
		percentage: 0
	},
	group3 : {
		name: "t > 1200 ms",
		count: 1256,
		percentage: 70
	},
	group4 : {
		name: "failed",
		count: 505,
		percentage: 28
	},
	meanNumberOfRequestsPerSecond: {
		total: "29",
		ok: "21",
		ko: "8"
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
