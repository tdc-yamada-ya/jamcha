# 出力ファイルについて

このツールは Java ソースコードを解析して、メソッド呼び出し階層に関するいくつかの情報を
複数の CSV ファイルに分けて出力します。
この文書では出力ファイルの内容について説明します。

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

-   [出力ファイル](#%E5%87%BA%E5%8A%9B%E3%83%95%E3%82%A1%E3%82%A4%E3%83%AB)
-   [ancestorSuperType.csv](#ancestorsupertypecsv)
-   [call.csv](#callcsv)
-   [callerAnnotation.csv](#callerannotationcsv)
-   [expandedCall.csv](#expandedcallcsv)
-   [superType.csv](#supertypecsv)
-   [typeAnnotation.csv](#typeannotationcsv)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## 出力ファイル

| ファイル名              | 説明                                               |
| ----------------------- | -------------------------------------------------- |
| `ancestorSuperType.csv` | 型の継承関係を祖先まで遡って全て出力します。       |
| `call.csv`              | メソッド呼び出しの親子関係を出力します。           |
| `callerAnnotation.csv`  | メソッド呼び出し元のアノテーション式を出力します。 |
| `expandedCall.csv`      | メソッド呼び出しを子孫まで辿って全て出力します。   |
| `superType.csv`         | 型の継承元を出力します。                           |
| `typeAnnotation.csv`    | 型のアノテーションを出力します。                   |

## ancestorSuperType.csv

型の継承元の祖先を CSV 形式で出力します。
例えば `java.lang.Double` は `java.lang.Number` と `java.lang.Object` を祖先に持つため、
それらを別々の行で出力します。

この CSV ファイルの各列の内容は以下の通りです。

| 列                               | 説明                   |
| -------------------------------- | ---------------------- |
| `name`                           | ファイルの相対パス     |
| `typePackageName`                | 型のパッケージ名       |
| `typeQualifiedName`              | 型の解決済み名         |
| `typeName`                       | 型の名前               |
| `typeBeginLine`                  | 型のソースコード開始行 |
| `typeBeginColumn`                | 型のソースコード開始列 |
| `ancestorSuperTypeQualifiedName` | 型の継承元の祖先       |

## call.csv

メソッド呼び出しの親子関係を CSV 形式で出力します。

この CSV ファイルの各列の内容は以下の通りです。

| 列                               | 説明                           |
| -------------------------------- | ------------------------------ |
| `name`                           | ファイルの相対パス             |
| `typePackageName`                | 型のパッケージ名               |
| `typeQualifiedName`              | 型の解決済み名                 |
| `typeName`                       | 型の名前                       |
| `callerLabel`                    | 呼び出し元のラベル             |
| `callerMethodQualifiedSignature` | 呼び出し元メソッドの解決済み名 |
| `callerMethodSignature`          | 呼び出し元メソッドの名前       |
| `callerBeginLine`                | 呼び出し元のソースコード開始行 |
| `callerBeginColumn`              | 呼び出し元のソースコード開始列 |
| `calleePackageName`              | 呼び出し先のパッケージ名       |
| `calleeTypeQualifiedName`        | 呼び出し先型の解決済み名       |
| `calleeTypeName`                 | 呼び出し先型の名前             |
| `calleeMethodQualifiedSignature` | 呼び出し先メソッドの解決済み名 |
| `calleeMethodSignature`          | 呼び出し先メソッドの名前       |
| `calleeBeginLine`                | 呼び出し先のソースコード開始行 |
| `calleeBeginColumn`              | 呼び出し先のソースコード開始列 |

## callerAnnotation.csv

呼び出し元のアノテーションを CSV 形式で出力します。

呼び出し元がメソッド内の場合はそのメソッドのアノテーションを、
メンバ変数の初期化の場合はメンバ変数のアノテーションを出力します。

この CSV ファイルの各列の内容は以下の通りです。

| 列                               | 説明                           |
| -------------------------------- | ------------------------------ |
| `name`                           | ファイルの相対パス             |
| `typePackageName`                | 型のパッケージ名               |
| `typeQualifiedName`              | 型の解決済み名                 |
| `typeName`                       | 型の名前                       |
| `callerLabel`                    | 呼び出し元のラベル             |
| `callerMethodQualifiedSignature` | 呼び出し元メソッドの解決済み名 |
| `callerMethodSignature`          | 呼び出し元メソッドの名前       |
| `callerBeginLine`                | 呼び出し元のソースコード開始行 |
| `callerBeginColumn`              | 呼び出し元のソースコード開始列 |
| `callerAnnotationExpr`           | 呼び出し元のアノテーション式   |

## expandedCall.csv

メソッド呼び出しの子孫まで辿って CSV に出力します。

例えば A メソッド内で B メソッドを、B メソッド内で C メソッドを呼び出す場合、
A メソッドから見た子孫は B メソッドと C メソッドのため、
この二つをそれぞれの行に出力します。

また A インタフェースを継承した B クラスがあり、
C クラスが A インタフェースのメソッドを呼び出す場合、
B クラスのインタフェースメソッドも呼び出される可能性があると判断して、
C クラスの子孫として A インタフェースと B クラスの両方を出力します。

この CSV ファイルの各列の内容は以下の通りです。

| 列                                   | 説明                               |
| ------------------------------------ | ---------------------------------- |
| `typeQualifiedName`                  | 型のパッケージ名                   |
| `callerLabel`                        | 呼び出し元のラベル                 |
| `descendantMethodQualifiedSignature` | 呼び出し先子孫メソッドの解決済み名 |
| `route`                              | 子孫に到達するまでのルート         |

## superType.csv

型の継承元を CSV に出力します。

この CSV ファイルの各列の内容は以下の通りです。

| 列                       | 説明                   |
| ------------------------ | ---------------------- |
| `name`                   | ファイルの相対パス     |
| `typePackageName`        | 型のパッケージ名       |
| `typeQualifiedName`      | 型の解決済み名         |
| `typeName`               | 型の名前               |
| `typeBeginLine`          | 型のソースコード開始行 |
| `typeBeginColumn`        | 型のソースコード開始列 |
| `superTypeQualifiedName` | 型の継承元             |

## typeAnnotation.csv

型のアノテーションを CSV に出力します。

この CSV ファイルの各列の内容は以下の通りです。

| 列                   | 説明                   |
| -------------------- | ---------------------- |
| `name`               | ファイルの相対パス     |
| `typePackageName`    | 型のパッケージ名       |
| `typeQualifiedName`  | 型の解決済み名         |
| `typeName`           | 型の名前               |
| `typeBeginLine`      | 型のソースコード開始行 |
| `typeBeginColumn`    | 型のソースコード開始列 |
| `typeAnnotationExpr` | 型のアノテーション式   |
