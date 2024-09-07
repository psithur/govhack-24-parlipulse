with (import <nixpkgs> { });
mkShell {
  buildInputs = [
    google-cloud-sdk
    clojure
  ];
}
