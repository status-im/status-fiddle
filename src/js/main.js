window.deps = {
  'react' : require('react'),
  'react-dom' : require('react-dom'),
  'react-native-web' : require('react-native-web'),
  'qrcode.react' : require('qrcode.react')
};

window.React = window.deps['react'];
window.ReactDOM = window.deps['react-dom'];
window.ReactNativeWeb = window.deps['react-native-web'];
window.QRCode = window.deps['qrcode.react'];
