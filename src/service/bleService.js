const noble = require('@abandonware/noble');

class BLEService {
    constructor() {
    }

    discoverAndPublish(mqttClient) {
        const RSSI_THRESHOLD = -90;
        const EXIT_GRACE_PERIOD = 2000; // milliseconds

        const inRange = [];

        noble.on('discover', function (peripheral) {
            if (peripheral.rssi < RSSI_THRESHOLD) {
                // ignore
                return;
            }

            const id = peripheral.id;
            const entered = !inRange[id];

            if (entered) {
                inRange[id] = {
                    peripheral
                };

                var peripheralLocalName = peripheral.advertisement.localName;

                // console.log(`${peripheralLocalName} - ${peripheral.rssi}`);

                if (peripheralLocalName) {
                    mqttClient.publish("tracing", peripheralLocalName);
                }
            }

            inRange[id].lastSeen = Date.now();
        });

        setInterval(function () {
            for (const id in inRange) {
                if (inRange[id].lastSeen < Date.now() - EXIT_GRACE_PERIOD) {
                const peripheral = inRange[id].peripheral;

                var peripheralLocalName = peripheral.advertisement.localName;

                if (peripheralLocalName != undefined) {
                    // console.log(
                    //   `"${peripheral.advertisement.localName}" exited (RSSI ${
                    //     peripheral.rssi
                    //   }) ${new Date()}`
                    // );
                }
                delete inRange[id];
                }
            }
        }, EXIT_GRACE_PERIOD / 2);

        noble.on('stateChange', function (state) {
            if (state === 'poweredOn') {
                noble.startScanning([], true);
            } else {
                noble.stopScanning();
            }
        });

        process.on('SIGINT', function () {
            console.log('Caught interrupt signal');
            noble.stopScanning(() => process.exit());
        });

        process.on('SIGQUIT', function () {
            console.log('Caught interrupt signal');
            noble.stopScanning(() => process.exit());
        });

        process.on('SIGTERM', function () {
            console.log('Caught interrupt signal');
            noble.stopScanning(() => process.exit());
        });
    }
}

module.exports = BLEService;