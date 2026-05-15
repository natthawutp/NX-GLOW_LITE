# GWH_TM_EDIC
#Entity #Standard #MASTER

| # | Column Name | Data Type | Nullable | Description |
|---|------------|-----------|----------|-------------|
| 1 | DEL_FLG | CHAR(1) | N | Delete Flag / 論理削除フラグ |
| 2 | CRT_YMD | CHAR(8) | N | Create Date (UTC) / 作成日(標準時) |
| 3 | CRT_TIM | CHAR(6) | N | Create HMS (UTC) / 作成時分秒(標準時) |
| 4 | CRT_TMID | NVARCHAR2(200) | N | Create Term Id / 作成端末ID |
| 5 | CRT_USER | NVARCHAR2(100) | N | Create User Id / 作成ユーザID |
| 6 | CRT_PGM | NVARCHAR2(60) | N | Create Program Id / 作成プログラムID |
| 7 | CRT_TM_ZONE | NVARCHAR2(6) | N | Create User Time Zone / 作成ユーザタイムゾーン |
| 8 | CRT_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時） |
| 9 | CRT_L_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル） |
| 10 | UPD_YMD | CHAR(8) | N | Update Date (UTC) / 更新日(標準時) |
| 11 | UPD_TIM | CHAR(6) | N | Update HMS (UTC) / 更新時分秒(標準時) |
| 12 | UPD_TMID | NVARCHAR2(200) | N | Update Terminal ID / 更新端末ID |
| 13 | UPD_USER | NVARCHAR2(100) | N | Update User ID / 更新ユーザID |
| 14 | UPD_PGM | NVARCHAR2(60) | N | Update Program ID / 更新プログラムID |
| 15 | UPD_TM_ZONE | NVARCHAR2(6) | N | Update User Time Zone / 更新ユーザタイムゾーン |
| 16 | UPD_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時） |
| 17 | UPD_L_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル） |
| 18 | EDIC_EDI_CUST_COD | NVARCHAR2(100) | N | EDI Customer Code / EDI顧客コード |
| 19 | EDIC_DFLT_FLG | CHAR(1) | N | EDI Default Flag / デフォルト設定FLG |
| 20 | EDIC_PRC_KND | CHAR(2) | N | Processing Kind / 処理区分 |
| 21 | EDIC_DAT_KND | CHAR(2) | N | Data Kind / データ種別 |
| 22 | EDIC_MAX_CNT | NUMBER(7) | N | Maximum Number / 処理上限件数 |
| 23 | EDIC_MAX_THRD | NUMBER(2) | N | Maximum Thread Number / 処理上限スレッド数 |
| 24 | EDIC_CTL_KND | CHAR(2) | N | Control Kind / 制御区分 |
| 25 | EDIC_SND_MAX_CNT | NUMBER(7) | N | Send Maximum Number / 送信処理上限件数 |
| 26 | EDIC_SND_MAX_THRD | NUMBER(2) | N | Send Maximum Thread Number / 送信処理上限スレッド数 |
| 27 | EDIC_SND_CTL_KND | CHAR(2) | N | Send Control Kind / 送信制御区分 |
| 28 | EDIC_CPNY_COD | NVARCHAR2(12) | Y | Company Code / カンパニーコード |
| 29 | EDIC_WHS_COD | NVARCHAR2(8) | Y | Warehouse Code / 倉庫コード |
| 30 | EDIC_CUST_COD | NVARCHAR2(26) | Y | Customer Code / 顧客コード |
| 31 | EDIC_SCDL_COD | NVARCHAR2(40) | Y | Schedule Cod / スケジュールコード |
| 32 | EDIC_SND_FILE_DIR | NVARCHAR2(400) | N | Send File Directory / 送信ファイル保管ディレクトリ |
| 33 | EDIC_SND_FILE_NAM | NVARCHAR2(100) | N | Send File Name / 送信ファイル名 |
| 34 | EDIC_SND_ERR_MAX_CNT | NUMBER(3) | N | Send Error Maximum Number / 送信エラー上限回数 |
| 35 | EDIC_FLOW_NAM | NVARCHAR2(1000) | Y | Customize Flow Name / カスタマイズフロー名 |
| 36 | EDIC_ERR_STOP_FLG | CHAR(1) | N | Error Stop Flag / エラー停止フラグ |
| 37 | EDIC_SND_FLOW_NAM | NVARCHAR2(1000) | Y | Send Customize Flow Name / 送信カスタマイズフロー名 |
| 38 | EDIC_EIN_COD | CHAR(3) | Y | Character Encoding(IN) for Screen / 画面用文字コード(IN) |
| 39 | EDIC_EOUT_COD | CHAR(3) | Y | Character Encoding(OUT) for Screen / 画面用文字コード(OUT) |
| 40 | EDIC_NWNL_COD | CHAR(3) | Y | Newline Code for Screen / 画面用改行コード |
| 41 | EDIC_EMP_SKIP_FLG | CHAR(1) | N | Empty File Skip Flag / 空ファイルスキップフラグ |
| 42 | EDIC_RCV_EMP_ERR_FLG | CHAR(1) | N | Receive Empty  File Error Flag / 受信空ファイルエラーフラグ |
| 43 | EDIC_REG_FLG | CHAR(1) | N | Register Control Flag / 処理対象登録フラグ |
| 44 | EDIC_REG_MAX_CNT | NUMBER(4) | N | File Register Maximum Count / 処理対象登録ファイル上限数 |
| 45 | EDIC_REG_THRD | NUMBER(2) | N | File Register Maximum Thread Number / 処理対象登録上限スレッド数 |
| 46 | EDIC_REG_FILE_DIR | NVARCHAR2(400) | Y | Register File Directory / 処理対象登録ファイル保管ディレクトリ |
| 47 | EDIC_REG_FLOW_NAM | NVARCHAR2(1000) | Y | Register Customize Flow Name / 処理対象登録カスタマイズフロー名 |
| 48 | EDIC_PRC_NUM_PFIX | NVARCHAR2(4) | Y | Processing number Prefix / 処理No.プレフィックス |
| 49 | EDIC_PRC_NUM_SET | NVARCHAR2(60) | Y | Processing number Settings / 処理No.採番設定 |
