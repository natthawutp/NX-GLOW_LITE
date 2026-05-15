# GWH_TJ_GWES_HIS
#Entity #Standard #HISTORY

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
| 18 | GWEH_CTR_NUM | NUMBER(4) | N | Control number / 管理番号 |
| 19 | GWEH_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 20 | GWEH_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 21 | GWEH_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 22 | GWEH_PRC_KEY | NVARCHAR2(1000) | N | Processing key / 処理key |
| 23 | GWEH_WK_KND | CHAR(2) | N | Work Kind / 作業区分 |
| 24 | GWEH_WK_STS | CHAR(3) | N | Work Status / 作業ステータス |
| 25 | GWEH_PRS_KND | CHAR(2) | N | Processing Status Kind / 処理状況区分 |
| 26 | GWEH_OUT_KND | CHAR(2) | N | output Kind / 出力区分 |
| 27 | GWEH_TRS_QTY | NVARCHAR2(20) | Y | Number of transmissions / 送信回数 |
| 28 | GWEH_FLE_PTH | NVARCHAR2(800) | Y | File Path / ファイルパス |
| 29 | GWEH_ERR_RMKS | NVARCHAR2(400) | Y | Error detail / エラー詳細 |
