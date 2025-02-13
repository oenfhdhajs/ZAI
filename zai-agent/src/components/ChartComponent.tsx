import * as echarts from "echarts";
import {useEffect} from "react";
import BigNumber from "bignumber.js";

interface Props {
    list: any[];
}

const ChartComponent: React.FC<Props> = ({list}) => {
    const chartRef = useRef(null);
    let lineChart: echarts.ECharts;
    let chartFirstPrice: any;
    let chartLastPrice: any;

    const initChart = () => {
        if (list.length === 0) return;
        const date = [];
        const data: any[] = [];
        
        for (const item of list) {
            // let d = new Date(item.lastTimestamp);
            // date.push([String(d.getHours()).padStart(2, "0"), String(d.getMinutes()).padStart(2, "0")].join("-"));
            date.push(item.lastTimestamp);
            data.push(item.closePrice);
        }
        chartFirstPrice = data[0];
        chartLastPrice = data[data.length - 1];

        // let base = +new Date(1968, 9, 3);
        // const oneDay = 24 * 3600 * 1000;
        // const date = [];
        // const data = [Math.random() * 300];
        // for (let i = 1; i < 20000; i++) {
        //     const now = new Date((base += oneDay));
        //     date.push([now.getFullYear(), now.getMonth() + 1, now.getDate()].join('/'));
        //     data.push(Math.round((Math.random() - 0.5) * 20 + data[i - 1]));
        // }

        const convertedData = date.map((timestamp, index) => [timestamp, data[index]]);

        const max = Number(Math.max(...data));
        const min = Number(Math.min(...data));
        if (!lineChart && chartRef.current) {
            lineChart = echarts.init(chartRef.current, null, {
                renderer: "svg",
                useDirtyRect: false,
            });
            // setChartListener();
        }
        const option = {
            // animation: false,
            tooltip: {
                trigger: "axis",
                show: true,
                showContent: true,
                confine: true,
                transitionDuration: 0,
                position: function (point: [any], _params: any, _dom: any, _rect: any, size: { contentSize: any; viewSize: any; }) {
                    const [x] = point;
                    const { contentSize, viewSize } = size;
                    const [boxWidth] = contentSize;
                    const [viewWidth] = viewSize;
                    let posX = 0;
                    const offset = 5;
                    if (x > viewWidth / 2) {
                        posX = x - boxWidth - offset;
                    } else {
                        posX = x + offset;
                    }
                    return [posX, "10%"];
                },
                axisPointer: {
                    type: "line",
                    axis: "x",
                },
                // formatter: (params: any[]) => {
                //     // console.log("chartFormatter:", params[0].data);
                //     selectTokenPrice.value = formatPrice(params[0].data[1], 6);
                //     let firstPrice = data[0];
                //     let priceChange = params[0].data[1] - firstPrice;
                //     selectPriceChange.value = priceChange.toString();
                //     selectRisePercent.value = toDecimalsMul(toDecimalsDiv(priceChange, firstPrice), 100);
                //     // console.log("chartFormatter selectRisePercent:", selectRisePercent.value);
                //     let content = "";
                //     switch (type) {
                //         case 1:
                //         case 2:
                //         case 3:
                //             content = `${formatTimestampToHours(params[0].data[0]).toString()}`;
                //             break;
                //         case 4:
                //             content = `${formatTimestampToMouth(params[0].data[0]).toString()}`;
                //             break;
                //         case 5:
                //         case 6:
                //         case 7:
                //             content = `${formatTimestampToYear(params[0].data[0]).toString()}`;
                //             break;
                //     }
                //     return content;
                // },
            },
            grid: {
                left: 0,
                right: 0,
                top: "10px",
                bottom: "10px",
                containLabel: true,
            },
            xAxis: {
                type: "time",
                boundaryGap: "0.1%",
                // data: date,
                axisLine: {
                    show: false,
                },
                axisTick: {
                    show: false,
                },
                axisLabel: {
                    show: false,
                    margin: 10,
                    interval: 0,
                },
                splitLine: {
                    show: false,
                },
            },
            yAxis: {
                type: "value",
                boundaryGap: 0,
                axisLine: {
                    show: false,
                },
                axisTick: {
                    show: false,
                },
                axisLabel: {
                    show: false,
                    margin: 0,
                    interval: 0,
                },
                splitLine: {
                    show: false,
                },
                axisPointer: {
                    show: false,
                },
                max: max,
                min: min,
                interval: (max - min) / 5,
                splitNumber: 5,
            },
            series: [
                {
                    name: "",
                    symbol: "none",
                    type: "line",
                    sampling: "lttb",
                    itemStyle: {
                        color: getChartStatus() === 1 ? "#29CA53" : "#E42170",
                    },
                    areaStyle: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                            {
                                offset: 0,
                                color: getChartStatus() === 1 ? "rgba(0, 192, 87, 0.2)" : "rgba(249, 32, 111, 0.2)",
                            },
                            {
                                offset: 1,
                                color: getChartStatus() === 1 ? "rgba(0, 192, 87, 0)" : "rgba(249, 32, 111, 0)",
                            },
                        ]),
                    },
                    data: convertedData,
                },
                {
                    type: "effectScatter",
                    coordinateSystem: "cartesian2d",
                    data: [],
                    showEffectOn: "render",
                    rippleEffect: {
                        brushType: "stroke",
                    },
                    // hoverAnimation: true,
                    emphasis: {
                        scale: true,
                    },
                    itemStyle: {
                        color: getChartStatus() === 1 ? "#29CA53" : "#E42170",
                        shadowBlur: 30,
                        shadowColor: getChartStatus() === 1 ? "#29CA53" : "#E42170",
                    },
                    zlevel: 1,
                },
            ],
        };
        if (lineChart && option && typeof option === "object") {
            lineChart.setOption(option);
        }
    };

    function getChartStatus(): number {
        const price = new BigNumber(chartLastPrice).toFixed();
        const priceChange = Number(price) - chartFirstPrice;
        if (priceChange > 0) {
            return 1;
        } else if (priceChange < 0) {
            return 2;
        } else {
            return 0;
        }
    }

    useEffect(() => {
        initChart();
    }, []);

    return (
        <div ref={chartRef} id="container" className="w-full h-220px"/>
    )
}

export default ChartComponent;