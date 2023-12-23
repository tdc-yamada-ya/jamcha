# jamcha

_JAva Method Call Hierarchy Analyzer_

コードレビューを補助するために、
Java ソースコードを解析してメソッドの呼び出し階層を再帰的に分析可能なテーブルを出力する
コマンドラインツールです。

このツールを使って得られた出力を活用することで、以下のようなレビューの補助を行うことができます。

-   特定のメソッドの全ての呼び出し元をリスト化する
-   特定のメソッドが呼び出している全てのメソッドをリスト化する

このツールは [JavaParser] を用いてソースコードを解析しています。

## ツール入出力例

[入力ファイル: 複数の Java ソースコード](./examples/basic/src)

[出力ファイル: 複数の CSV ファイル](./examples/basic/.reports)

## 出力ファイルについて

出力ファイルの内容については、 [こちらのドキュメント](./docs/about-outputs.md) を参照してください。

## ビルド

`gradle shadowJar` で依存関係を含む jar ファイルを作成してください。
jar ファイルは `build/libs` ディレクトリに出力されます。

## 実行

```bash
$ java -cp build/libs/jamcha-0.0.1-all.jar \
  -i "解析ソースディレクトリ1" "解析ソースディレクトリ2" ... "解析ソースディレクトリn" \
  -s "依存ソースディレクトリ1" "依存ソースディレクトリ2" ... "依存ソースディレクトリn" \
  -l "依存ライブラリディレクトリ1" "依存ライブラリディレクトリ2" ... "依存ライブラリディレクトリn" \
  -o "出力ディレクトリ"
```

**解析ソースディレクトリ**:
解析するソースコードが含まれるディレクトリを指定してください。
複数のディレクトリを指定することができます。
特定のパッケージのソースコードだけを分析したい場合は、
該当するパッケージのディレクトリのみを指定することが可能です。

**依存ソースディレクトリ**:
完全なクラス名やメソッド名を解決するために、依存関係にあるソースコードが含まれるディレクトリを指定してください。
複数のディレクトリを指定することができます。
基本的には、解析対象のソースコードもこの依存ソースディレクトリに指定してください。

**依存ライブラリディレクトリ**
完全なクラス名やメソッド名を解決するために、
依存関係にある jar ファイルが含まれるディレクトリを指定してください。
複数のディレクトリを指定することができます。

**出力ディレクトリ**
出力ファイルを格納するディレクトリを指定してください。
ディレクトリが存在しない場合は自動的に作成します。

## Docker 実行

[GitHub Container Registry](https://github.com/users/tdc-yamada-ya/packages/container/package/jamcha) にてこのツールのイメージを公開しています。

以下の例は最新版 (`latest` タグ) を使用します。
常に最新版を使用する場合は事前に `docker pull ghcr.io/tdc-yamada-ya/jamcha` を実行してください。
安定版を使用する場合は `ghcr.io/tdc-yamada-ya/jamcha:main` を指定してください。

```bash
$ docker run \
  -v "/path/to/src:/src" \
  -v "/path/to/libs:/libs" \
  -v "/path/to/reports:/reports" \
  ghcr.io/tdc-yamada-ya/jamcha \
  -i /src \
  -s /src \
  -l /libs \
  -o /reports
```

解析対象が巨大なために JVM メモリが不足する場合は `docker run` のオプションに
[`--entrypoint`](https://docs.docker.com/engine/reference/run/#entrypoint-default-command-to-execute-at-runtime)
を指定して、JVM メモリを拡張してください。

```bash
$ docker run \
  -v "/path/to/src:/src" \
  -v "/path/to/libs:/libs" \
  -v "/path/to/reports:/reports" \
  ghcr.io/tdc-yamada-ya/jamcha \
  -Xmx2048m -cp ./jamcha.jar jp.co.tdc.jamcha.cmd.Main \
  -i /src \
  -s /src \
  -l /libs \
  -o /reports
```

## 制限事項

-   [JavaParser] の TypeSolver が未対応のため `var` 変数のメソッド呼び出し階層を出力することができません。

[javaparser]: https://github.com/javaparser/javaparser
