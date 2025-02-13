import app from './app';

const port = Number.parseInt(process.env.PORT!);
const server = app.listen(port, '0.0.0.0', 1024, () => {
  /* eslint-disable no-console */
  console.log(`Listening: http://localhost:${port}`);
  /* eslint-enable no-console */
});
server.maxConnections = 10000;
