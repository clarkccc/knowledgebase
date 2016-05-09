-- 用于将一张表的一个字段赋值给另一张表，两张表通过ID来链接
-- 这里的复杂性ID会查出两个结果，而赋值只能赋值一个
MERGE INTO T_COVER_DEVICE a
USING (
        SELECT
          DEVICE_ID,
          ORG_ID
        FROM
          (SELECT
             DEVICE_ID,
             ORG_ID,
             row_number()
             OVER
               (PARTITION BY DEVICE_ID
               ORDER BY ORG_ID DESC) AS aw
           FROM T_COVER
          WHERE DEVICE_ID IS NOT NULL AND ORG_ID IS NOT NULL)
        WHERE aw = 1
      ) t
ON (a.DEVICE_ID = t.DEVICE_ID)
WHEN MATCHED THEN
UPDATE
SET a.ORG_ID=t.ORG_ID
