import { Metric } from 'web-vitals'; // Changed import from ReportCallback to Metric

const reportWebVitals = (onPerfEntry?: (metric: Metric) => void) => { // Typed onPerfEntry directly
  if (onPerfEntry && onPerfEntry instanceof Function) {
    import('web-vitals').then(({ getCLS, getFID, getFCP, getLCP, getTTFB }) => {
      getCLS(onPerfEntry);
      getFID(onPerfEntry);
      getFCP(onPerfEntry);
      getLCP(onPerfEntry);
      getTTFB(onPerfEntry);
    });
  }
};

export default reportWebVitals; // Added default export