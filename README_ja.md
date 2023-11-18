# Squall

[![build](https://github.com/openclosed-dev/squall/actions/workflows/build.yml/badge.svg)](https://github.com/openclosed-dev/squall/actions/workflows/build.yml)

Squall は、Javadoc/JSDoc スタイルのコメントが付けられた DDL/SQL ソースコードからデータベース設計書を生成するためのツールとライブラリのスイートです。

## 機能

* データベースサーバーを実行せずに、DDL ソースを直接解析します。
* HTML、PDF、AsciiDoc、Markdown、JSON などのさまざまな形式でデータベース設計を記述するドキュメントを生成します。
* スキーマ オブジェクトに注釈を付けるための特別なタグを含む、Javadoc/JSDoc スタイルのDocコメントを認識します。
* CLI ツールと JVM 言語用のAPIの両方を提供します。
* 日本語の出力に対応しています。

現在サポートされている唯一の SQL 言語は PostgreSQL であることに注意してください。

## Docコメント

Doc コメントは、`/**` で始まり `*/` で終わる複数行のコメントです。
このタイプのコメントは、データベース、スキーマ、シーケンス、テーブル、テーブル列などのオブジェクトの CREATE ステートメントが開始される直前に、これらのオブジェクトに注釈を付けるために使用できます。
以下の例は、Docコメントがデータベーススキーマ内のテーブルとカラムにどのように適用されるかを示しています。

```sql
/**
 * MLBの野球チーム。
 * @label 野球チーム
 */
CREATE TABLE baseball_team(
  /**
   * チームの一意なID。
   * @label ID
   */
  id integer PRIMARY KEY,
  /**
   * チームの名前。
   * @label 名前
   */
  name varchar(128) NOT NULL,
  /**
   * チームが所属するリーグ。
   * @label 所属リーグ
   */
  affiliated_league char(2) NOT NULL REFERENCES league(id),
  /**
   * チームが設立された年。
   * @label Year 設立年
   */
  year_established integer NOT NULL
);
```

タグに先行するドキュメント コメント内の説明部分は、Markdown、より具体的には [CommonMark](https://commonmark.org/) のコンテンツとして扱われます。

Docコメントには、対象のスキーマオブジェクトに注釈を付けるための 1 つ以上のタグを含めることができます。

| タグ | 説明 |
| --- | --- |
| `@label` | オブジェクトの論理名を定義します。表示用。 |
| `@since` | オブジェクトが最初に追加されたときのバージョンを指定します。 |
| `@deprecated` | オブジェクトが非推奨であり、間もなく削除されることを警告します。 |


## インストール

このプロジェクトの[リリースページ](https://github.com/openclosed-dev/squall/releases)では、リリースごとに次の種類の配布ファイルを提供します。

* Windows 向けの MSI フォーマット
* Windows 向けの ZIP フォーマット

すべての配布ファイルには 64 ビットの Java ランタイムが含まれています。 32 ビットプラットフォームをサポートする予定はありません。

インストールが完了したら、コンソール ウィンドウを閉じて再度開き、インストール処理中に変更された PATH 環境変数を再ロードする必要があります。

zip 形式が便利な場合は、アーカイブを任意の場所に解凍し、そのディレクトリを PATH 環境変数に追加するだけです。

インストールが成功したことは、次のコマンドを実行することで確認できます。
```
squall -v
```

## CLIを使う

サポートされているプラットフォームのインストールまたは解凍されたパッケージには、`squall` という名前の実行可能ファイルが含まれています

このコマンドには、以下に示すサブコマンドが用意されています。

| サブコマンド | 説明 |
| --- | --- |
| config | 設定を管理します。 |
| spec | データベース設計を管理します。 |

### config サブコマンド

次のコマンドの呼び出しにより、現在のディレクトリに `squall.json` という名前の初期設定ファイルが生成されます。

```
squall config init
```

まず、DDL ソース ファイルを JSON ファイル内の `sources` プロパティに追加する必要があります。

```json
{
  "sources": [
    "your-ddl-file.sql",
    "another-ddl-file.sql"
  ],
  ...
}
```

パスは絶対パスでも、設定ファイルに対する相対パスでもかまいません。

エントリの順序は重要です。 パーサーは、設定ファイル内のエントリとまったく同じ順序でソースファイルを 1 つずつ処理します。 ソースファイルは、それが依存するファイルがすべて処理された後に解析する必要があります。

### spec サブコマンド

データベース設計書は、次のように `spec render` コマンドを呼び出すことによって、解析された DDL ソースから生成できます。

```
squall spec render <レンダラー名>
```

レンダラーのセットは設定ファイルで事前定義されています。 選択したレンダラーが設定内で `default`という名前になっている場合は、次のようにコマンドラインで名前を省略できます。

```
squall spec render
```

複数のレンダラーを同じ行に指定できます。 この場合、指定されたレンダラーが 1 つずつ実行されます。

```
squall spec render default pdf markdown
```

## 将来の計画

* テストデータを生成するためのより豊富なタグセット。例. `@format email`
* ER図の自動生成。　
* Checkstyle がプログラムコードに対して行っているような、データベース設計の検証。
* より多くの SQL 言語のサポート。

## 著作権表示

Copyright 2022-2023 The Squall Authors. All rights reserved.

このソフトウェアは、[Apache License, Version 2.0][Apache 2.0 License]に基づいてライセンス供与されています。

[Apache 2.0 License]: https://www.apache.org/licenses/LICENSE-2.0
